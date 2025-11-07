package com.monitoramento.tracking.api.mapper;

import com.monitoramento.tracking.api.dto.LocationHistoryResponseDTO;
import com.monitoramento.tracking.domain.model.LocationDataPoint;
import org.mapstruct.Mapper;

@Mapper
public interface LocationHistoryMapper {
    LocationHistoryResponseDTO toResponseDTO(LocationDataPoint location);
}
