package com.monitoramento.asset.api.dto;

import java.util.UUID;

public record SimpleDriverDTO(
        UUID id,
        String fullName
) {
}