package com.monitoramento.shared.pub.api.dto;

import com.monitoramento.transit.domain.model.enums.DayOfWeekProfile;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record PublicScheduleDTO(
        UUID id,
        DayOfWeekProfile dayProfile,
        List<LocalTime> departureTimes
) {
}
