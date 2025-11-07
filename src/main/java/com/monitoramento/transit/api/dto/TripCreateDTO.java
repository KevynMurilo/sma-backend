package com.monitoramento.transit.api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record TripCreateDTO(
        @NotNull
        Long scheduleDepartureId,
        @NotNull
        LocalDate tripDate,
        @NotNull
        UUID assetId,
        @NotNull
        UUID driverId
) {
}