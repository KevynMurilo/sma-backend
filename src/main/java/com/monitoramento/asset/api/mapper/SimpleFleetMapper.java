package com.monitoramento.asset.api.mapper;

import com.monitoramento.asset.api.dto.SimpleFleetDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Fleet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {DepartmentMapper.class})
public interface SimpleFleetMapper {
    SimpleFleetDTO fleetToSimpleFleetDTO(Fleet fleet);
}