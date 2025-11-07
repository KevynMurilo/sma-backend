package com.monitoramento.asset.api.mapper;

import com.monitoramento.asset.api.dto.VehicleDetailsDTO;
import com.monitoramento.asset.domain.model.VehicleDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleDetailsMapper {
    VehicleDetailsDTO vehicleDetailsToVehicleDetailsDTO(VehicleDetails vehicleDetails);
}