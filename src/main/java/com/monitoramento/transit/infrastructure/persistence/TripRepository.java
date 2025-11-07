package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID>, JpaSpecificationExecutor<Trip> {

    @Query("SELECT t FROM Trip t " +
            "JOIN FETCH t.scheduleDeparture sd " +
            "JOIN FETCH sd.schedule s " +
            "JOIN FETCH s.route r " +
            "WHERE t.driver.id = :driverId " +
            "AND t.tripDate = :tripDate " +
            "AND t.status IN :statuses")
    List<Trip> findByDriverAndDateAndStatus(UUID driverId, LocalDate tripDate, List<TripStatus> statuses);

    Optional<Trip> findByIdAndDriverId(UUID tripId, UUID driverId);
}