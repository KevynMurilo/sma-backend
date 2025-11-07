package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.StopDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.domain.model.Stop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StopMapper {
    StopResponseDTO toResponseDTO(Stop stop);

    @Mapping(target = "id", ignore = true)
    Stop toEntity(StopDTO dto);
}