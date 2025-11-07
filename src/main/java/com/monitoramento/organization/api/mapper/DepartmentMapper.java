package com.monitoramento.organization.api.mapper;

import com.monitoramento.organization.api.dto.DepartmentRequestDTO;
import com.monitoramento.organization.api.dto.DepartmentResponseDTO;
import com.monitoramento.organization.domain.model.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fleets", ignore = true)
    Department requestToDepartment(DepartmentRequestDTO requestDTO);

    DepartmentResponseDTO departmentToResponseDTO(Department department);
}