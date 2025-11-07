package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.organization.api.dto.FleetCreateDTO;
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

@Service
@RequiredArgsConstructor
public class CreateFleetUseCase {

    private final FleetRepository fleetRepository;
    private final DepartmentRepository departmentRepository;
    private final FleetMapper fleetMapper;

    @Transactional
    public ApiResponseDTO<FleetResponseDTO> execute(FleetCreateDTO dto) {
        Department department = departmentRepository.findById(dto.departmentId())
                .orElse(null);

        if (department == null) {
            return ApiResponseDTO.error(404, "Departamento não encontrado.");
        }

        if (fleetRepository.findByNameAndDepartmentId(dto.name(), dto.departmentId()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe uma frota com este nome neste departamento.");
        }

        Fleet fleet = fleetMapper.fleetCreateDTOToFleet(dto, department);
        Fleet savedFleet = fleetRepository.save(fleet);
        FleetResponseDTO responseDTO = fleetMapper.fleetToFleetResponseDTO(savedFleet);

        return ApiResponseDTO.success(201, "Frota criada com sucesso", responseDTO);
    }
}