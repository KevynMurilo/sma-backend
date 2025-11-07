package com.monitoramento.tracking.domain.service;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.util.GeoUtils;
import com.monitoramento.tracking.api.dto.IngestionRequest;
import com.monitoramento.tracking.api.mapper.LocationDataPointMapper;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import com.monitoramento.tracking.domain.model.LocationDataPoint;
import com.monitoramento.tracking.domain.model.enums.AssetStatus;
import com.monitoramento.tracking.infrastructure.persistence.AssetCurrentStatusRepository;
import com.monitoramento.tracking.infrastructure.persistence.LocationDataPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrackingEventListener {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final LocationDataPointRepository locationDataPointRepository;
    private final AssetCurrentStatusRepository assetCurrentStatusRepository;
    private final MonitoredAssetRepository monitoredAssetRepository;
    private final LocationDataPointMapper locationDataPointMapper;

    private static final double MIN_SPEED_MOVING = 5.0;
    private static final long IDLE_THRESHOLD_MINUTES = 10;

    @Async
    @EventListener
    @Transactional
    public void handleIngestionEvent(IngestionRequest request) {
        log.debug("Processando evento de ingestão para: {}", request.deviceSerial());

        TrackingDevice device = trackingDeviceRepository.findByDeviceSerial(request.deviceSerial())
                .orElseThrow(() -> new RuntimeException("Device not found: " + request.deviceSerial()));
        MonitoredAsset asset = monitoredAssetRepository.findByTrackingDeviceId(device.getId())
                .orElseThrow(() -> new RuntimeException("Asset not found for device: " + device.getId()));

        if (device.getStatus() == DeviceStatus.UNASSIGNED || device.getStatus() == DeviceStatus.MAINTENANCE) {
            log.warn("Dispositivo {} está com status {} e não pode receber dados.", device.getDeviceSerial(), device.getStatus());
            return;
        }

        LocationDataPoint locationDataPoint = locationDataPointMapper.toEntity(request, device.getId());
        LocationDataPoint savedLocation = locationDataPointRepository.save(locationDataPoint);

        updateDeviceStatus(device);

        upsertAssetCurrentStatus(asset, savedLocation);
    }

    private void updateDeviceStatus(TrackingDevice device) {
        if (device.getStatus() != DeviceStatus.ONLINE) {
            device.setStatus(DeviceStatus.ONLINE);
            trackingDeviceRepository.save(device);
        }
    }

    private void upsertAssetCurrentStatus(MonitoredAsset asset, LocationDataPoint location) {
        AssetCurrentStatus currentStatus = assetCurrentStatusRepository.findById(asset.getId())
                .orElseGet(() -> createNewAssetStatus(asset));

        AssetStatus newStatus = calculateAssetStatus(location, currentStatus);

        currentStatus.setLastLatitude(location.getLatitude());
        currentStatus.setLastLongitude(location.getLongitude());
        currentStatus.setLastUpdated(location.getTimestamp());
        currentStatus.setLastLocation(location);
        currentStatus.setStatus(newStatus);

        assetCurrentStatusRepository.save(currentStatus);
    }

    private AssetCurrentStatus createNewAssetStatus(MonitoredAsset asset) {
        AssetCurrentStatus newStatus = new AssetCurrentStatus();
        newStatus.setAsset(asset);
        newStatus.setId(asset.getId());
        return newStatus;
    }

    private AssetStatus calculateAssetStatus(LocationDataPoint newLocation, AssetCurrentStatus oldStatus) {
        if (newLocation.getSpeed() != null && newLocation.getSpeed() >= MIN_SPEED_MOVING) {
            return AssetStatus.MOVING;
        }

        if (oldStatus.getLastLocation() == null) {
            return AssetStatus.STOPPED;
        }

        double distance = GeoUtils.calculateHaversineDistance(
                oldStatus.getLastLatitude(), oldStatus.getLastLongitude(),
                newLocation.getLatitude(), newLocation.getLongitude()
        );

        if (distance < 10) {
            long minutesIdle = ChronoUnit.MINUTES.between(oldStatus.getLastUpdated(), newLocation.getTimestamp());
            if (minutesIdle >= IDLE_THRESHOLD_MINUTES) {
                return AssetStatus.IDLE;
            }
        }

        return AssetStatus.STOPPED;
    }
}