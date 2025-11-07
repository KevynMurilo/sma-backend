package com.monitoramento.organization.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FleetUpdateDTO(
        @NotBlank String name,
        @NotNull UUID departmentId
) {
}