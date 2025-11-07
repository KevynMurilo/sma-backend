package com.monitoramento.transit.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StopDTO(
        @NotBlank
        String name,
        String description,
        @NotNull
        double latitude,
        @NotNull
        double longitude
) {
}