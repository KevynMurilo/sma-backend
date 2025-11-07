package com.monitoramento.organization.api.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequestDTO(
        @NotBlank String name,
        @NotBlank String code
) {
}