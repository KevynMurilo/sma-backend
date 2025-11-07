package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteFleetUseCase {

    private final FleetRepository fleetRepository;
    private final MonitoredAssetRepository monitoredAssetRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID fleetId) {
        if (!fleetRepository.existsById(fleetId)) {
            return ApiResponseDTO.empty(404, "Frota não encontrada");
        }

        if (monitoredAssetRepository.existsByFleetId(fleetId)) {
            return ApiResponseDTO.error(409, "Não é possível excluir a frota pois ela possui veículos (ativos) associados.");
        }

        fleetRepository.deleteById(fleetId);
        return ApiResponseDTO.success(200, "Frota deletada com sucesso");
    }
}