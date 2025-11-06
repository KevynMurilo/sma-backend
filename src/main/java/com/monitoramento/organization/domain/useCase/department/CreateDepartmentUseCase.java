package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public ApiResponseDTO<DepartmentDTO> execute(DepartmentDTO dto) {
        try {
            Department department = departmentMapper.departmentDTOToDepartment(dto);
            Department saved = departmentRepository.save(department);
            DepartmentDTO dtoMapped = departmentMapper.departmentToDepartmentDTO(saved);
            return ApiResponseDTO.success(201, "Departamento criado com sucesso", dtoMapped);
        } catch (Exception e) {
            return ApiResponseDTO.error(500, "Erro ao criar departamento: " + e.getMessage());
        }
    }
}
