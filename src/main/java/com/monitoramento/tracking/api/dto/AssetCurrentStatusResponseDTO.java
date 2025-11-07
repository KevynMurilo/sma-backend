package com.monitoramento.tracking.api.dto;

import com.monitoramento.tracking.domain.model.enums.AssetStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssetCurrentStatusResponseDTO(
        UUID assetId,
        String assetName,
        double lastLatitude,
        double lastLongitude,
        OffsetDateTime lastUpdated,
        AssetStatus status
) {
}