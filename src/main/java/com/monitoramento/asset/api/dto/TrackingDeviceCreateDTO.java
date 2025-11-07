package com.monitoramento.asset.api.dto;

import jakarta.validation.constraints.NotBlank;

public record TrackingDeviceCreateDTO(
        @NotBlank(message = "O serial do dispositivo é obrigatório")
        String deviceSerial,

        @NotBlank(message = "O modelo do dispositivo é obrigatório")
        String model
) {
}