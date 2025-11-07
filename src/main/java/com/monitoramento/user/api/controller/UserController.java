package com.monitoramento.user.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.user.api.dto.UserCreateDTO;
import com.monitoramento.user.api.dto.UserPermissionUpdateDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.dto.UserUpdateDTO;
import com.monitoramento.user.domain.useCase.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final ListUsersUseCase listUsersUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UpdateUserPermissionsUseCase updateUserPermissionsUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> createUser(@Valid @RequestBody UserCreateDTO dto) {
        ApiResponseDTO<UserResponseDTO> response = createUserUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<UserResponseDTO>>> listUsers(@PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<UserResponseDTO>> response = listUsersUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getUserById(@PathVariable UUID id) {
        ApiResponseDTO<UserResponseDTO> response = getUserByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateDTO dto) {
        ApiResponseDTO<UserResponseDTO> response = updateUserUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}/permissions")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> updateUserPermissions(@PathVariable UUID id, @Valid @RequestBody UserPermissionUpdateDTO dto) {
        ApiResponseDTO<UserResponseDTO> response = updateUserPermissionsUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}