package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.organization.api.dto.FleetUpdateDTO;
import com.monitoramento.organization.api.dto.FleetResponseDTO;
import com.monitoramento.organization.api.mapper.FleetMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateFleetUseCase {

    private final FleetRepository fleetRepository;
    private final DepartmentRepository departmentRepository;
    private final FleetMapper fleetMapper;

    @Transactional
    public ApiResponseDTO<FleetResponseDTO> execute(UUID fleetId, FleetUpdateDTO dto) {
        Fleet fleet = fleetRepository.findById(fleetId)
                .orElse(null);

        if (fleet == null) {
            return ApiResponseDTO.empty(404, "Frota não encontrada");
        }

        Department department = departmentRepository.findById(dto.departmentId())
                .orElse(null);

        if (department == null) {
            return ApiResponseDTO.error(404, "Departamento não encontrado.");
        }

        if (fleetRepository.findByNameAndDepartmentIdAndIdNot(dto.name(), dto.departmentId(), fleetId).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe uma frota com este nome neste departamento.");
        }

        fleet.setName(dto.name());
        fleet.setDepartment(department);

        Fleet savedFleet = fleetRepository.save(fleet);
        FleetResponseDTO responseDTO = fleetMapper.fleetToFleetResponseDTO(savedFleet);

        return ApiResponseDTO.success(200, "Frota atualizada com sucesso", responseDTO);
    }
}