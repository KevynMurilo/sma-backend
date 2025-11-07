package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.security.JwtService;
import com.monitoramento.user.api.dto.AuthRequestDTO;
import com.monitoramento.user.api.dto.AuthResponseDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.domain.model.RefreshToken;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.domain.service.RefreshTokenService;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final AuditService auditService;

    public ApiResponseDTO<AuthResponseDTO> execute(AuthRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.login(), dto.password())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtService.generateToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Create refresh token
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

            User user = new User();
            user.setId(userDetails.getId());
            user.setUsername(userDetails.getUsername());
            user.setEnabled(userDetails.isEnabled());

            UserResponseDTO userDTO = userMapper.userToUserResponseDTO(user);
            AuthResponseDTO authResponse = new AuthResponseDTO(token, refreshToken.getToken(), userDTO);

            // Audit log
            auditService.log(AuditAction.LOGIN, AuditEntity.USER, userDetails.getId().toString(),
                    "User logged in successfully");

            return ApiResponseDTO.success(200, "Login realizado com sucesso", authResponse);
        } catch (Exception e) {
            // Audit failed login
            auditService.log(AuditAction.LOGIN_FAILED, AuditEntity.USER, dto.login(),
                    "Failed login attempt: " + e.getMessage());
            return ApiResponseDTO.error(401, "Credenciais inválidas: " + e.getMessage());
        }
    }
}