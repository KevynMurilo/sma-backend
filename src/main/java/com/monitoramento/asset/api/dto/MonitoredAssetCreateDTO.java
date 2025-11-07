package com.monitoramento.asset.api.dto;

import com.monitoramento.asset.domain.model.enums.AssetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record MonitoredAssetCreateDTO(
        @NotBlank(message = "O nome do ativo é obrigatório")
        String name,

        @NotNull(message = "O tipo do ativo é obrigatório")
        AssetType type,

        @NotNull(message = "A frota é obrigatória")
        UUID fleetId,

        boolean isPubliclyVisible,

        String licensePlate,
        String model,
        String make,
        Integer year
) {
}