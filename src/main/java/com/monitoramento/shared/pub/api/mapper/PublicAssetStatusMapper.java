package com.monitoramento.shared.pub.api.mapper;

import com.monitoramento.shared.pub.api.dto.PublicAssetStatusDTO;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PublicAssetStatusMapper {

    @Mapping(target = "assetId", source = "id")
    @Mapping(target = "assetName", source = "asset.name")
    @Mapping(target = "assetType", source = "asset.type")
    PublicAssetStatusDTO toDTO(AssetCurrentStatus status);
}