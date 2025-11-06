package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public ApiResponseDTO<DepartmentDTO> execute(UUID id, DepartmentDTO dto) {
        return departmentRepository.findById(id)
                .map(existing -> {
                    existing.setName(dto.name());
                    existing.setCode(dto.code());
                    Department updated = departmentRepository.save(existing);
                    DepartmentDTO dtoMapped = departmentMapper.departmentToDepartmentDTO(updated);
                    return ApiResponseDTO.success(200, "Departamento atualizado", dtoMapped);
                })
                .orElseGet(() -> ApiResponseDTO.error(404, "Departamento não encontrado"));
    }
}
