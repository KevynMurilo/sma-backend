package com.monitoramento.organization.domain.useCase.department;

import com.monitoramento.organization.api.dto.DepartmentResponseDTO;
import com.monitoramento.organization.api.mapper.DepartmentMapper;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetAllDepartmentUseCase {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<DepartmentResponseDTO>> execute(Pageable pageable) {
        Page<Department> departments = departmentRepository.findAll(pageable);
        Page<DepartmentResponseDTO> dtoPage = departments.map(departmentMapper::departmentToResponseDTO);
        PagedResponseDTO<DepartmentResponseDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Departamentos encontrados", paged);
    }
}