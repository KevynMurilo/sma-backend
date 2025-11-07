package com.monitoramento.asset.api.mapper;

import com.monitoramento.asset.api.dto.MonitoredAssetCreateDTO;
import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.VehicleDetails;
import com.monitoramento.organization.domain.model.Fleet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SimpleFleetMapper.class, SimpleDriverMapper.class, TrackingDeviceMapper.class})
public interface MonitoredAssetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "trackingDevice", ignore = true)
    @Mapping(target = "currentDriver", ignore = true)
    @Mapping(target = "publiclyVisible", source = "dto.isPubliclyVisible")
    @Mapping(target = "fleet", source = "fleet")
    @Mapping(target = "name", source = "dto.name")
    MonitoredAsset assetCreateDTOToMonitoredAsset(MonitoredAssetCreateDTO dto, Fleet fleet);

    MonitoredAssetResponseDTO monitoredAssetToMonitoredAssetResponseDTO(MonitoredAsset asset);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "asset", ignore = true)
    VehicleDetails assetCreateDTOToVehicleDetails(MonitoredAssetCreateDTO dto);
}