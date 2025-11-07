package com.monitoramento.shared.dto;

import com.monitoramento.tracking.domain.model.enums.AssetStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
public class ManagerAssetStatusDTO {
    private UUID assetId;
    private String assetName;
    private String currentDriverFullName;
    private AssetStatus status;
    private double lastLatitude;
    private double lastLongitude;
    private OffsetDateTime lastUpdated;
    private String fleetName;
    private String departmentName;
}