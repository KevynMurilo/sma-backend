package com.monitoramento.transit.api.mapper;

import com.monitoramento.transit.api.dto.RouteStopAssignmentResponseDTO;
import com.monitoramento.transit.domain.model.RouteStopAssignment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StopMapper.class})
public interface RouteStopAssignmentMapper {
    RouteStopAssignmentResponseDTO toResponseDTO(RouteStopAssignment assignment);
}