package com.monitoramento.asset.api.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AssignDeviceDTO(
        @NotNull(message = "O ID do dispositivo é obrigatório")
        UUID deviceId
) {
}