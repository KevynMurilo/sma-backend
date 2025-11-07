package com.monitoramento.user.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDTO(
        @NotBlank
        String fullName,

        @Email
        String email,

        boolean enabled
) {
}