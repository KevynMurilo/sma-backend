package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.api.dto.VehicleDetailsDTO;
import com.monitoramento.asset.api.mapper.VehicleDetailsMapper;
import com.monitoramento.asset.infrastructure.persistence.VehicleDetailsRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVehicleDetailsByAssetIdUseCase {

    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final VehicleDetailsMapper vehicleDetailsMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<VehicleDetailsDTO> execute(UUID assetId) {
        return vehicleDetailsRepository.findById(assetId)
                .map(vehicleDetailsMapper::vehicleDetailsToVehicleDetailsDTO)
                .map(dto -> ApiResponseDTO.success(200, "Detalhes do veículo encontrados", dto))
                .orElseGet(() -> ApiResponseDTO.empty(404, "Detalhes do veículo não encontrados para este ativo"));
    }
}