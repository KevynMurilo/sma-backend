package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.RouteStopAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteStopAssignmentRepository extends JpaRepository<RouteStopAssignment, Long> {
    Optional<RouteStopAssignment> findByRouteIdAndStopOrder(UUID routeId, int stopOrder);
    Optional<RouteStopAssignment> findByRouteIdAndStopId(UUID routeId, UUID stopId);
    List<RouteStopAssignment> findByStopId(UUID stopId);
}