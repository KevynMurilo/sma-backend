package com.monitoramento.asset.api.mapper;

import com.monitoramento.asset.api.dto.TrackingDeviceCreateDTO;
import com.monitoramento.asset.api.dto.TrackingDeviceResponseDTO;
import com.monitoramento.asset.domain.model.TrackingDevice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrackingDeviceMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(com.monitoramento.asset.domain.model.enums.DeviceStatus.UNASSIGNED)")
    @Mapping(target = "assignedAsset", ignore = true)
    TrackingDevice trackingDeviceCreateDTOToTrackingDevice(TrackingDeviceCreateDTO dto);

    @Mapping(target = "assignedAssetId", source = "assignedAsset.id")
    TrackingDeviceResponseDTO trackingDeviceToTrackingDeviceResponseDTO(TrackingDevice device);
}