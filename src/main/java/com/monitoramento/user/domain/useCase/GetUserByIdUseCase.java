package com.monitoramento.user.domain.useCase;

import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<UserResponseDTO> execute(UUID userId) {
        return userRepository.findById(userId)
                .map(userMapper::userToUserResponseDTO)
                .map(dto -> ApiResponseDTO.success(200, "Usuário encontrado", dto))
                .orElseGet(() -> ApiResponseDTO.empty(404, "Usuário não encontrado"));
    }
}