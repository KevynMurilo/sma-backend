package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.ScheduleDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.domain.model.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ScheduleDepartureMapper.class})
public interface ScheduleMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "route", source = "route")
    @Mapping(target = "departures", ignore = true)
    Schedule toEntity(ScheduleDTO dto, Route route);

    @Mapping(target = "routeId", source = "route.id")
    ScheduleResponseDTO toResponseDTO(Schedule schedule);
}