package com.monitoramento.organization.api.mapper;

import com.monitoramento.organization.api.dto.FleetDTO;
import com.monitoramento.organization.domain.model.Fleet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FleetMapper {
    FleetDTO toDTO(Fleet fleet);
    Fleet toEntity(FleetDTO fleetDTO);
}
