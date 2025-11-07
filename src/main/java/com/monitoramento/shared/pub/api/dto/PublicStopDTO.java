package com.monitoramento.shared.pub.api.dto;

import java.util.UUID;

public record PublicStopDTO(
        UUID id,
        String name,
        String description,
        double latitude,
        double longitude,
        int stopOrder
) {
}
