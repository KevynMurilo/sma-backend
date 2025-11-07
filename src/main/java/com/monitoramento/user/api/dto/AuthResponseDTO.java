package com.monitoramento.user.api.dto;

public record AuthResponseDTO(
        String token,
        String refreshToken,
        UserResponseDTO user
) {
}