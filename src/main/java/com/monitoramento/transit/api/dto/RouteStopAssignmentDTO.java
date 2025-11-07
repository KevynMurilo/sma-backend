package com.monitoramento.transit.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record RouteStopAssignmentDTO(
        @NotNull
        UUID stopId,
        @NotNull @Positive
        int stopOrder
) {
}