package com.monitoramento.asset.api.dto;

import com.monitoramento.asset.domain.model.enums.AssetType;
import java.util.UUID;

public record MonitoredAssetResponseDTO(
        UUID id,
        String name,
        AssetType type,
        boolean isPubliclyVisible,
        SimpleFleetDTO fleet,
        SimpleDriverDTO currentDriver,
        SimpleTrackingDeviceDTO trackingDevice
) {
}