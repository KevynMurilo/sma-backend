package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.SimpleRouteDTO;
import com.monitoramento.transit.domain.model.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleRouteMapper {
    SimpleRouteDTO toSimpleRouteDTO(Route route);
}