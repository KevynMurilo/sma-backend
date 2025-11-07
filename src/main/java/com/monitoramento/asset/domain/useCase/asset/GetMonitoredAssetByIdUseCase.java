package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.api.mapper.MonitoredAssetMapper;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMonitoredAssetByIdUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(UUID assetId) {
        return monitoredAssetRepository.findById(assetId)
                .map(monitoredAssetMapper::monitoredAssetToMonitoredAssetResponseDTO)
                .map(dto -> ApiResponseDTO.success(200, "Ativo encontrado", dto))
                .orElseGet(() -> ApiResponseDTO.empty(404, "Ativo não encontrado"));
    }
}