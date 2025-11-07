package com.monitoramento.transit.api.dto;

import java.time.LocalTime;

public record ScheduleDepartureResponseDTO(
        Long id,
        LocalTime departureTime
) {
}