package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.security.JwtService;
import com.monitoramento.user.api.dto.AuthResponseDTO;
import com.monitoramento.user.api.dto.RefreshTokenRequestDTO;
import com.monitoramento.user.api.dto.UserResponseDTO;
import com.monitoramento.user.api.mapper.UserMapper;
import com.monitoramento.user.domain.model.RefreshToken;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.domain.service.RefreshTokenService;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUseCase {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public ApiResponseDTO<AuthResponseDTO> execute(RefreshTokenRequestDTO dto) {
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(dto.refreshToken())
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

            refreshTokenService.verifyExpiration(refreshToken);

            User user = refreshToken.getUser();
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            String newAccessToken = jwtService.generateToken(authentication);

            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getId());

            UserResponseDTO userDTO = userMapper.userToUserResponseDTO(user);
            AuthResponseDTO authResponse = new AuthResponseDTO(newAccessToken, newRefreshToken.getToken(), userDTO);

            return ApiResponseDTO.success(200, "Token refreshed successfully", authResponse);
        } catch (Exception e) {
            log.error("Error refreshing token", e);
            return ApiResponseDTO.error(401, "Invalid refresh token: " + e.getMessage());
        }
    }
}
