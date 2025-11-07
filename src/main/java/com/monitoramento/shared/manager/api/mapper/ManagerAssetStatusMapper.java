package com.monitoramento.shared.manager.api.mapper;

import com.monitoramento.shared.manager.api.dto.ManagerAssetStatusDTO;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ManagerAssetStatusMapper {

    @Mapping(target = "assetId", source = "id")
    @Mapping(target = "assetName", source = "asset.name")
    @Mapping(target = "currentDriverFullName", source = "asset.currentDriver.fullName")
    @Mapping(target = "fleetName", source = "asset.fleet.name")
    @Mapping(target = "departmentName", source = "asset.fleet.department.name")
    ManagerAssetStatusDTO toDTO(AssetCurrentStatus status);
}