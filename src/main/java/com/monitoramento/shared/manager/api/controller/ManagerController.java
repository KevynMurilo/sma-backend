package com.monitoramento.organization.api.controller;

import com.monitoramento.organization.domain.useCase.fleet.GetManagerFleetStatusUseCase;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.ManagerAssetStatusDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final GetManagerFleetStatusUseCase getManagerFleetStatusUseCase;

    @GetMapping("/fleet/status")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<ManagerAssetStatusDTO>>> getFleetStatus(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<PagedResponseDTO<ManagerAssetStatusDTO>> response = getManagerFleetStatusUseCase.execute(userDetails, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}