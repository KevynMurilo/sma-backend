package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.domain.service.RefreshTokenService;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutUseCase {

    private final RefreshTokenService refreshTokenService;
    private final AuditService auditService;

    public ApiResponseDTO<Void> execute() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
                refreshTokenService.revokeByUserId(userDetails.getId());

                auditService.log(AuditAction.LOGOUT, AuditEntity.USER, userDetails.getId().toString(),
                        "User logged out");

                SecurityContextHolder.clearContext();
                return ApiResponseDTO.success(200, "Logout realizado com sucesso");
            }
            return ApiResponseDTO.error(401, "No authenticated user found");
        } catch (Exception e) {
            log.error("Error during logout", e);
            return ApiResponseDTO.error(500, "Error during logout: " + e.getMessage());
        }
    }
}
