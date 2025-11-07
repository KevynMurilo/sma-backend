package com.monitoramento.transit.api.dto;

import com.monitoramento.asset.domain.model.enums.AssetType;
import java.util.UUID;

public record SimpleAssetDTO(
        UUID id,
        String name,
        AssetType type
) {}