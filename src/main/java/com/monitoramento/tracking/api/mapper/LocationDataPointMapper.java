package com.monitoramento.tracking.api.mapper;

import com.monitoramento.tracking.api.dto.IngestionRequest;
import com.monitoramento.tracking.domain.model.LocationDataPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface LocationDataPointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deviceId", source = "deviceId")
    LocationDataPoint toEntity(IngestionRequest request, UUID deviceId);
}