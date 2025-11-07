package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.api.mapper.MonitoredAssetMapper;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListMonitoredAssetsByFleetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final FleetRepository fleetRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<MonitoredAssetResponseDTO>> execute(UUID fleetId, Pageable pageable) {
        if (!fleetRepository.existsById(fleetId)) {
            return ApiResponseDTO.empty(404, "Frota não encontrada");
        }

        Page<MonitoredAssetResponseDTO> dtoPage = monitoredAssetRepository.findByFleetId(fleetId, pageable)
                .map(monitoredAssetMapper::monitoredAssetToMonitoredAssetResponseDTO);

        PagedResponseDTO<MonitoredAssetResponseDTO> pagedResponse = PagedResponseDTO.from(dtoPage);
        return ApiResponseDTO.success(200, "Ativos encontrados", pagedResponse);
    }
}