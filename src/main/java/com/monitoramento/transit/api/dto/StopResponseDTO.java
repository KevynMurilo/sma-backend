package com.monitoramento.transit.api.dto;

import java.util.UUID;

public record StopResponseDTO(
        UUID id,
        String name,
        String description,
        double latitude,
        double longitude
) {
}