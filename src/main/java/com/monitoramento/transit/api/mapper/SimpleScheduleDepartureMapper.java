package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.SimpleScheduleDepartureDTO;
import com.monitoramento.transit.domain.model.ScheduleDeparture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = SimpleRouteMapper.class)
public interface SimpleScheduleDepartureMapper {
    @Mapping(target = "route", source = "schedule.route")
    SimpleScheduleDepartureDTO toSimpleDTO(ScheduleDeparture scheduleDeparture);
}