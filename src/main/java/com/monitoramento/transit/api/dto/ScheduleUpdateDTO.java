package com.monitoramento.transit.api.dto;

import com.monitoramento.transit.domain.model.enums.DayOfWeekProfile;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record ScheduleUpdateDTO(
        @NotNull
        UUID routeId,
        @NotNull
        DayOfWeekProfile dayProfile
) {
}
