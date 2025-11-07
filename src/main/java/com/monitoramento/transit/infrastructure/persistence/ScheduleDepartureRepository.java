package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.ScheduleDeparture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleDepartureRepository extends JpaRepository<ScheduleDeparture, Long> {
    Optional<ScheduleDeparture> findByScheduleIdAndDepartureTime(UUID scheduleId, LocalTime departureTime);
}