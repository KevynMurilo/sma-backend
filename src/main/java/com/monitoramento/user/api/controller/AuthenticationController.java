package com.monitoramento.user.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.api.dto.AuthRequestDTO;
import com.monitoramento.user.api.dto.AuthResponseDTO;
import com.monitoramento.user.api.dto.RefreshTokenRequestDTO;
import com.monitoramento.user.domain.useCase.AuthenticationUseCase;
import com.monitoramento.user.domain.useCase.LogoutUseCase;
import com.monitoramento.user.domain.useCase.RefreshTokenUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationUseCase authenticationUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO dto) {
        ApiResponseDTO<AuthResponseDTO> response = authenticationUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<AuthResponseDTO>> refresh(@Valid @RequestBody RefreshTokenRequestDTO dto) {
        ApiResponseDTO<AuthResponseDTO> response = refreshTokenUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout() {
        ApiResponseDTO<Void> response = logoutUseCase.execute();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}