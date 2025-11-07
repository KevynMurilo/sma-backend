package com.monitoramento.user.domain.useCase;

import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<UserResponseDTO>> execute(Pageable pageable) {
        Page<UserResponseDTO> dtoPage = userRepository.findAll(pageable)
                .map(userMapper::userToUserResponseDTO);

        PagedResponseDTO<UserResponseDTO> pagedResponse = PagedResponseDTO.from(dtoPage);
        return ApiResponseDTO.success(200, "Usuários encontrados", pagedResponse);
    }
}