package com.monitoramento.tracking.domain.useCase;

import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.tracking.api.dto.LocationHistoryResponseDTO;
import com.monitoramento.tracking.api.mapper.LocationHistoryMapper;
import com.monitoramento.tracking.domain.model.LocationDataPoint;
import com.monitoramento.tracking.infrastructure.persistence.LocationDataPointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetLocationHistoryUseCase {

    private final LocationDataPointRepository locationDataPointRepository;
    private final TrackingDeviceRepository trackingDeviceRepository;
    private final LocationHistoryMapper locationHistoryMapper;

    public ApiResponseDTO<PagedResponseDTO<LocationHistoryResponseDTO>> execute(
            UUID assetId,
            OffsetDateTime startDate,
            OffsetDateTime endDate,
            Pageable pageable) {

        try {
            TrackingDevice device = trackingDeviceRepository.findByAssignedAssetId(assetId)
                    .orElseThrow(() -> new RuntimeException("No tracking device assigned to this asset"));

            Page<LocationDataPoint> locations;

            if (startDate != null && endDate != null) {
                locations = locationDataPointRepository.findByDeviceIdAndTimestampBetween(
                        device.getId(), startDate, endDate, pageable);
            } else if (startDate != null) {
                locations = locationDataPointRepository.findByDeviceIdAndTimestampAfter(
                        device.getId(), startDate, pageable);
            } else if (endDate != null) {
                locations = locationDataPointRepository.findByDeviceIdAndTimestampBefore(
                        device.getId(), endDate, pageable);
            } else {
                locations = locationDataPointRepository.findByDeviceId(device.getId(), pageable);
            }

            PagedResponseDTO<LocationHistoryResponseDTO> response = PagedResponseDTO.from(
                    locations.map(locationHistoryMapper::toResponseDTO)
            );

            return ApiResponseDTO.success(200, "Location history retrieved successfully", response);
        } catch (Exception e) {
            log.error("Error getting location history for asset {}", assetId, e);
            return ApiResponseDTO.error(500, "Error getting location history: " + e.getMessage());
        }
    }
}
