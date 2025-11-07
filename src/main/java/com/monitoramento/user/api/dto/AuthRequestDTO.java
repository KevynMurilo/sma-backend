package com.monitoramento.user.api.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequestDTO(
        @NotBlank
        String login,

        @NotBlank
        String password
) {
}