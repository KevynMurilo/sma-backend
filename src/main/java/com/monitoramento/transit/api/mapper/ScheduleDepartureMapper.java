package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.ScheduleDepartureDTO;
import com.monitoramento.transit.api.dto.ScheduleDepartureResponseDTO;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.domain.model.ScheduleDeparture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleDepartureMapper {
    ScheduleDepartureResponseDTO toResponseDTO(ScheduleDeparture departure);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "schedule", source = "schedule")
    ScheduleDeparture toEntity(ScheduleDepartureDTO dto, Schedule schedule);
}