package com.monitoramento.user.domain.useCase;

import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.api.dto.UserPermissionUpdateDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateUserPermissionsUseCase {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;

    @Transactional
    public ApiResponseDTO<UserResponseDTO> execute(UUID userId, UserPermissionUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ApiResponseDTO.empty(404, "Usuário não encontrado");
        }

        boolean isManager = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_MANAGER"));

        if (!isManager) {
            return ApiResponseDTO.error(403, "Este usuário não é um gerente e não pode ter departamentos gerenciáveis.");
        }

        Set<Department> departments = departmentRepository.findAllById(dto.departmentIds())
                .stream().collect(Collectors.toSet());

        if (departments.size() != dto.departmentIds().size()) {
            return ApiResponseDTO.error(400, "Um ou mais IDs de departamento são inválidos.");
        }

        user.setManageableDepartments(departments);
        User savedUser = userRepository.save(user);
        UserResponseDTO responseDTO = userMapper.userToUserResponseDTO(savedUser);

        return ApiResponseDTO.success(200, "Permissões do usuário atualizadas com sucesso", responseDTO);
    }
}