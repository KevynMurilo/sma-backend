package com.monitoramento.transit.infrastructure.persistence;

import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.domain.model.enums.DayOfWeekProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
    Optional<Schedule> findByRouteIdAndDayProfile(UUID routeId, DayOfWeekProfile dayProfile);
    List<Schedule> findByRouteId(UUID routeId);
}