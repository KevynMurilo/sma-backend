package com.monitoramento.asset.infrastructure.persistence;

import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackingDeviceRepository extends JpaRepository<TrackingDevice, UUID> {

    Optional<TrackingDevice> findByDeviceSerial(String deviceSerial);

    Optional<TrackingDevice> findByDeviceSerialAndIdNot(String deviceSerial, UUID id);

    Optional<TrackingDevice> findByAssignedAssetId(UUID assetId);

    Page<TrackingDevice> findByStatus(DeviceStatus status, Pageable pageable);
}