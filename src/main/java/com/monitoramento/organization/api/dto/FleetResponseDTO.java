package com.monitoramento.organization.api.dto;

import java.util.UUID;

public record FleetResponseDTO(
        UUID id,
        String name,
        DepartmentResponseDTO department
) {
}