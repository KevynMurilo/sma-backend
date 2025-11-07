package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.organization.api.dto.FleetResponseDTO;
import com.monitoramento.organization.api.mapper.FleetMapper;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
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
public class ListFleetsByDepartmentUseCase {

    private final FleetRepository fleetRepository;
    private final DepartmentRepository departmentRepository;
    private final FleetMapper fleetMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<FleetResponseDTO>> execute(UUID departmentId, Pageable pageable) {
        if (!departmentRepository.existsById(departmentId)) {
            return ApiResponseDTO.empty(404, "Departamento não encontrado");
        }

        Page<Fleet> fleetPage = fleetRepository.findByDepartmentId(departmentId, pageable);
        Page<FleetResponseDTO> dtoPage = fleetPage.map(fleetMapper::fleetToFleetResponseDTO);
        PagedResponseDTO<FleetResponseDTO> pagedResponse = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Frotas encontradas", pagedResponse);
    }
}