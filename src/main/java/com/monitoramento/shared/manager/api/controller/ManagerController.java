package com.monitoramento.shared.manager.api.controller;

import com.monitoramento.asset.api.dto.MonitoredAssetCreateDTO;
import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.manager.api.dto.ManagerAssetStatusDTO;
import com.monitoramento.shared.manager.domain.useCase.GetManagerFleetStatusUseCase;
import com.monitoramento.shared.manager.domain.useCase.ManagerCreateAssetUseCase;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final GetManagerFleetStatusUseCase getManagerFleetStatusUseCase;
    private final ManagerCreateAssetUseCase managerCreateAssetUseCase;

    @GetMapping("/fleet/status")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<ManagerAssetStatusDTO>>> getFleetStatus(
            @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<PagedResponseDTO<ManagerAssetStatusDTO>> response = getManagerFleetStatusUseCase.execute(userDetails, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/assets")
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> createAsset(
            @Valid @RequestBody MonitoredAssetCreateDTO dto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<MonitoredAssetResponseDTO> response = managerCreateAssetUseCase.execute(dto, userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}