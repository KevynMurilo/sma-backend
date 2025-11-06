package com.monitoramento.asset.infrastructure.persistence;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MonitoredAssetRepository extends JpaRepository<MonitoredAsset, UUID> {
}
