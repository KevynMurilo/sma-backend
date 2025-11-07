package com.monitoramento.asset.api.dto;

import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import java.util.UUID;

public record SimpleTrackingDeviceDTO(
        UUID id,
        String deviceSerial,
        String model,
        DeviceStatus status
) {
}