package com.monitoramento.transit.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ScheduleDepartureDTO(
        @NotNull
        LocalTime departureTime
) {
}