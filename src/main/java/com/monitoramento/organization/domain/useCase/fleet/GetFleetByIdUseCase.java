package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.organization.api.dto.FleetResponseDTO;
import com.monitoramento.organization.api.mapper.FleetMapper;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetFleetByIdUseCase {

    private final FleetRepository fleetRepository;
    private final FleetMapper fleetMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<FleetResponseDTO> execute(UUID fleetId) {
        return fleetRepository.findById(fleetId)
                .map(fleetMapper::fleetToFleetResponseDTO)
                .map(dto -> ApiResponseDTO.success(200, "Frota encontrada", dto))
                .orElseGet(() -> ApiResponseDTO.empty(404, "Frota não encontrada"));
    }
}