package com.monitoramento.transit.api.mapper;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.transit.api.dto.SimpleAssetDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SimpleAssetMapper {
    SimpleAssetDTO toSimpleDTO(MonitoredAsset asset);
}