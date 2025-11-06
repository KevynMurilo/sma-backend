package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAllDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public ApiResponseDTO<PagedResponseDTO<DepartmentDTO>> execute(Pageable pageable) {
        try {
            Page<Department> departments = departmentRepository.findAll(pageable);
            Page<DepartmentDTO> dtoPage = departments.map(departmentMapper::departmentToDepartmentDTO);
            PagedResponseDTO<DepartmentDTO> paged = PagedResponseDTO.from(dtoPage);
            return ApiResponseDTO.success(200, "Departamentos encontrados", paged);
        } catch (Exception e) {
            return ApiResponseDTO.error(500, "Erro ao buscar departamentos: " + e.getMessage());
        }
    }
}
