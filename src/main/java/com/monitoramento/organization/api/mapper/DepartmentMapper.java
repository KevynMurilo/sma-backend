package com.monitoramento.organization.api.mapper;

import com.monitoramento.organization.api.dto.DepartmentDTO;
import com.monitoramento.organization.domain.model.Department;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDTO departmentToDepartmentDTO(Department department);
    Department departmentDTOToDepartment(DepartmentDTO departmentDTO);
}
