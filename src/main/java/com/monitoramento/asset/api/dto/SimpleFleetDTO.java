package com.monitoramento.asset.api.dto;

import com.monitoramento.organization.api.dto.DepartmentResponseDTO;

import java.util.UUID;

public record SimpleFleetDTO(
        UUID id,
        String name,
        DepartmentResponseDTO department
) {
}