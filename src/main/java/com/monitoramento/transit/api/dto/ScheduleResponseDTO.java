package com.monitoramento.transit.api.dto;

import com.monitoramento.transit.domain.model.enums.DayOfWeekProfile;
import java.util.List;
import java.util.UUID;

public record ScheduleResponseDTO(
        UUID id,
        UUID routeId,
        DayOfWeekProfile dayProfile,
        List<ScheduleDepartureResponseDTO> departures
) {
}