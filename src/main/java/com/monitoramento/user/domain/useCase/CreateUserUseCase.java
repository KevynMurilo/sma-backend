package com.monitoramento.user.domain.useCase;

import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.infrastructure.persistence.DepartmentRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.api.dto.UserCreateDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.domain.model.Role;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.infrastructure.persistence.RoleRepository;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ApiResponseDTO<UserResponseDTO> execute(UserCreateDTO dto) {
        if (userRepository.existsByUsername(dto.username())) {
            return ApiResponseDTO.error(409, "Nome de usuário já cadastrado.");
        }
        if (userRepository.existsByCpf(dto.cpf())) {
            return ApiResponseDTO.error(409, "CPF já cadastrado.");
        }

        Set<Role> roles = roleRepository.findByNameIn(dto.roles());
        if (roles.size() != dto.roles().size()) {
            return ApiResponseDTO.error(400, "Um ou mais papéis (Roles) fornecidos são inválidos.");
        }

        boolean isManager = roles.stream().anyMatch(role -> role.getName().equals("ROLE_MANAGER"));

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(passwordEncoder.encode(dto.password()));
        user.setCpf(dto.cpf());
        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setRoles(roles);
        user.setEnabled(true);

        if (isManager) {
            Set<Department> allDepartments = departmentRepository.findAll().stream().collect(Collectors.toSet());
            user.setManageableDepartments(allDepartments);
        }

        User savedUser = userRepository.save(user);
        UserResponseDTO responseDTO = userMapper.userToUserResponseDTO(savedUser);

        return ApiResponseDTO.success(201, "Usuário criado com sucesso", responseDTO);
    }
}