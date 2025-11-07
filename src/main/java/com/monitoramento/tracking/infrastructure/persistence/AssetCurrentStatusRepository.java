package com.monitoramento.tracking.infrastructure.persistence;

import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssetCurrentStatusRepository extends JpaRepository<AssetCurrentStatus, UUID> {

    @Query(value = "SELECT acs FROM AssetCurrentStatus acs " +
            "JOIN FETCH acs.asset a " +
            "LEFT JOIN FETCH a.currentDriver " +
            "JOIN FETCH a.fleet f " +
            "JOIN FETCH f.department d " +
            "JOIN User mu ON mu.id = :userId " +
            "WHERE d IN elements(mu.manageableDepartments)",

            countQuery = "SELECT COUNT(acs) FROM AssetCurrentStatus acs " +
                    "JOIN acs.asset a " +
                    "JOIN a.fleet f " +
                    "JOIN f.department d " +
                    "JOIN User mu ON mu.id = :userId " +
                    "WHERE d IN elements(mu.manageableDepartments)")
    Page<AssetCurrentStatus> findManagerFleetStatus(@Param("userId") UUID userId, Pageable pageable);

    @Query(value = "SELECT acs FROM AssetCurrentStatus acs " +
            "JOIN FETCH acs.asset a " +
            "WHERE a.isPubliclyVisible = true",
            countQuery = "SELECT COUNT(acs) FROM AssetCurrentStatus acs " +
                    "JOIN acs.asset a " +
                    "WHERE a.isPubliclyVisible = true")
    Page<AssetCurrentStatus> findPublicLiveAssets(Pageable pageable);

}