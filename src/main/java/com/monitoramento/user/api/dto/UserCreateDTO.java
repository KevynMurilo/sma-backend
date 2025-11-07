package com.monitoramento.user.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

import java.util.Set;

public record UserCreateDTO(
        @NotBlank @Size(min = 4, max = 50)
        String username,

        @NotBlank @Size(min = 8, max = 100)
        String password,

        @NotBlank @CPF
        String cpf,

        @NotBlank
        String fullName,

        @Email
        String email,

        @NotNull
        Set<String> roles
) {
}