package com.monitoramento.organization.api.mapper;

import com.monitoramento.organization.api.dto.FleetCreateDTO;
import com.monitoramento.organization.api.dto.FleetResponseDTO;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.domain.model.Fleet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface FleetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", source = "department")
    @Mapping(target = "name", source = "dto.name")
    Fleet fleetCreateDTOToFleet(FleetCreateDTO dto, Department department);

    FleetResponseDTO fleetToFleetResponseDTO(Fleet fleet);
}