package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {
    Optional<Route> findByRouteName(String routeName);
}