package com.monitoramento.asset.api.dto;

import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import java.util.UUID;

public record TrackingDeviceResponseDTO(
        UUID id,
        String deviceSerial,
        String model,
        DeviceStatus status,
        UUID assignedAssetId
) {
}