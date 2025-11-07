package com.monitoramento.asset.api.dto;

import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TrackingDeviceUpdateDTO(
        @NotBlank(message = "O modelo do dispositivo é obrigatório")
        String model,

        @NotNull(message = "O status do dispositivo é obrigatório")
        DeviceStatus status
) {
}