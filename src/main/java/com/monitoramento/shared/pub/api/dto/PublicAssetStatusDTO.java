package com.monitoramento.shared.pub.api.dto;

import com.monitoramento.asset.domain.model.enums.AssetType;
import com.monitoramento.tracking.domain.model.enums.AssetStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class PublicAssetStatusDTO {
    private UUID assetId;
    private String assetName;
    private AssetType assetType;
    private AssetStatus status;
    private double lastLatitude;
    private double lastLongitude;
    private OffsetDateTime lastUpdated;
}