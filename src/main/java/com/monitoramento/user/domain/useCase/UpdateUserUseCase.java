package com.monitoramento.user.domain.useCase;

import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.dto.UserUpdateDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public ApiResponseDTO<UserResponseDTO> execute(UUID userId, UserUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return ApiResponseDTO.empty(404, "Usuário não encontrado");
        }

        user.setFullName(dto.fullName());
        user.setEmail(dto.email());
        user.setEnabled(dto.enabled());

        User savedUser = userRepository.save(user);
        UserResponseDTO responseDTO = userMapper.userToUserResponseDTO(savedUser);

        return ApiResponseDTO.success(200, "Usuário atualizado com sucesso", responseDTO);
    }
}