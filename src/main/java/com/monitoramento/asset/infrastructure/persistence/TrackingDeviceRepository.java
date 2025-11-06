package com.monitoramento.asset.infrastructure.persistence;

import com.monitoramento.asset.domain.model.TrackingDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrackingDeviceRepository extends JpaRepository<TrackingDevice, UUID> {
}
