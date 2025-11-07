package com.monitoramento.transit.api.mapper;

import com.monitoramento.asset.api.mapper.SimpleDriverMapper;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.domain.model.Trip;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        SimpleScheduleDepartureMapper.class,
        SimpleAssetMapper.class,
        SimpleDriverMapper.class
})
public interface TripMapper {
    TripResponseDTO toResponseDTO(Trip trip);
}