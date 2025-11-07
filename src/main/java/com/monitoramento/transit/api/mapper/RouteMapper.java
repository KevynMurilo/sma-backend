package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.RouteDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.domain.model.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RouteStopAssignmentMapper.class})
public interface RouteMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "stops", ignore = true)
    Route toEntity(RouteDTO dto);

    RouteResponseDTO toResponseDTO(Route route);
}