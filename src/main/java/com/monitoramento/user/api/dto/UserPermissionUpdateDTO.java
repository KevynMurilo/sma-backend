package com.monitoramento.user.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public record UserPermissionUpdateDTO(
        @NotNull
        Set<UUID> departmentIds
) {
}