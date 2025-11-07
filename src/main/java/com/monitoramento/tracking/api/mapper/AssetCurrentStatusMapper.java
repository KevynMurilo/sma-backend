package com.monitoramento.tracking.api.mapper;

import com.monitoramento.tracking.api.dto.AssetCurrentStatusResponseDTO;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetCurrentStatusMapper {

    @Mapping(target = "assetId", source = "id")
    @Mapping(target = "assetName", source = "asset.name")
    AssetCurrentStatusResponseDTO toDTO(AssetCurrentStatus status);
}