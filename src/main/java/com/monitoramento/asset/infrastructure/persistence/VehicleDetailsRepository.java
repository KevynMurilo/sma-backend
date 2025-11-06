package com.monitoramento.asset.infrastructure.persistence;

import com.monitoramento.asset.domain.model.VehicleDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleDetailsRepository extends JpaRepository<VehicleDetails, UUID> {
}
