package com.monitoramento.asset.infrastructure.persistence;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonitoredAssetRepository extends JpaRepository<MonitoredAsset, UUID> {

    boolean existsByFleetId(UUID fleetId);

    Page<MonitoredAsset> findByFleetId(UUID fleetId, Pageable pageable);

    Optional<MonitoredAsset> findByTrackingDeviceId(UUID trackingDeviceId);
}