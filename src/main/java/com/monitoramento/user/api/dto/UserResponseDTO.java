package com.monitoramento.user.api.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String username,
        String cpf,
        String fullName,
        String email,
        boolean enabled,
        Set<RoleDTO> roles,
        Set<UUID> manageableDepartmentIds
) {
}