package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentRequestDTO;
import com.monitoramento.organization.api.dto.DepartmentResponseDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional
    public ApiResponseDTO<DepartmentResponseDTO> execute(DepartmentRequestDTO dto) {
        if (departmentRepository.existsByNameOrCode(dto.name(), dto.code())) {
            return ApiResponseDTO.error(409, "Já existe um departamento com este nome ou código.");
        }

        Department department = departmentMapper.requestToDepartment(dto);
        Department saved = departmentRepository.save(department);
        DepartmentResponseDTO responseDTO = departmentMapper.departmentToResponseDTO(saved);

        return ApiResponseDTO.success(201, "Departamento criado com sucesso", responseDTO);
    }
}