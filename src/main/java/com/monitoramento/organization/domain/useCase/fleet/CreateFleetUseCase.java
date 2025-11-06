package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.organization.api.dto.FleetDTO;
import com.monitoramento.organization.api.mapper.FleetMapper;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateFleetUseCase {

    private final FleetRepository fleetRepository;
    private final FleetMapper fleetMapper;

    public ApiResponseDTO<FleetDTO> execute(FleetDTO dto) {
        try {
            Fleet fleet = fleetMapper.toEntity(dto);
            Fleet saved = fleetRepository.save(fleet);
            FleetDTO dtoMapped = fleetMapper.toDTO(saved);
            return ApiResponseDTO.success(201, "Frota criada com sucesso", dtoMapped);
        } catch (Exception e) {
            return ApiResponseDTO.error(500, "Erro ao criar frota: " + e.getMessage());
        }
    }
}
