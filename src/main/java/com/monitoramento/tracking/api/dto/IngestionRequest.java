package com.monitoramento.tracking.api.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record IngestionRequest(
        @NotNull
        String deviceSerial,

        @NotNull
        double latitude,

        @NotNull
        double longitude,

        Double speed,
        Double heading,
        Double altitude,

        @NotNull
        OffsetDateTime timestamp
) {
}