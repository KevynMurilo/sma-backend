package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.Stop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StopRepository extends JpaRepository<Stop, UUID> {
    Optional<Stop> findByName(String name);
}