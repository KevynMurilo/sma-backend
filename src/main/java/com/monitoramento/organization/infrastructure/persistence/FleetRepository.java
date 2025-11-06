package com.monitoramento.organization.infrastructure.persistence;

import com.monitoramento.organization.domain.model.Fleet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FleetRepository extends JpaRepository<Fleet, UUID> {
}
