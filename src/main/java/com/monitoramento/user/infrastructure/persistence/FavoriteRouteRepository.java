package com.monitoramento.user.infrastructure.persistence;

import com.monitoramento.user.domain.model.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, UUID> {

    @Query("SELECT fr FROM FavoriteRoute fr JOIN FETCH fr.route r WHERE fr.user.id = :userId")
    List<FavoriteRoute> findByUserId(UUID userId);

    Optional<FavoriteRoute> findByUserIdAndRouteId(UUID userId, UUID routeId);

    boolean existsByUserIdAndRouteId(UUID userId, UUID routeId);
}