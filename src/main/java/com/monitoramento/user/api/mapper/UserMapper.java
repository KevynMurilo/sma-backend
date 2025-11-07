package com.monitoramento.user.api.mapper;

import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.user.api.dto.RoleDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.domain.model.Role;
import com.monitoramento.user.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    RoleDTO roleToRoleDTO(Role role);

    @Mapping(target = "manageableDepartmentIds", source = "manageableDepartments")
    UserResponseDTO userToUserResponseDTO(User user);

    default Set<UUID> mapDepartmentsToIds(Set<Department> departments) {
        if (departments == null) {
            return Set.of();
        }
        return departments.stream()
                .map(Department::getId)
                .collect(Collectors.toSet());
    }
}