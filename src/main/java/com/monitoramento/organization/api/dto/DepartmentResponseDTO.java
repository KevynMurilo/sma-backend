package com.monitoramento.organization.api.dto;

import java.util.UUID;

public record DepartmentResponseDTO(
        UUID id,
        String name,
        String code
) {
}