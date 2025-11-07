package com.monitoramento.shared.pub.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicAssetStatusDTO;
import com.monitoramento.shared.pub.api.dto.PublicRouteDTO;
import com.monitoramento.shared.pub.api.dto.PublicScheduleDTO;
import com.monitoramento.shared.pub.domain.useCase.GetPublicLiveAssetsUseCase;
import com.monitoramento.shared.pub.domain.useCase.GetPublicRouteByIdUseCase;
import com.monitoramento.shared.pub.domain.useCase.GetPublicRouteSchedulesUseCase;
import com.monitoramento.shared.pub.domain.useCase.ListPublicRoutesUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final GetPublicLiveAssetsUseCase getPublicLiveAssetsUseCase;
    private final ListPublicRoutesUseCase listPublicRoutesUseCase;
    private final GetPublicRouteByIdUseCase getPublicRouteByIdUseCase;
    private final GetPublicRouteSchedulesUseCase getPublicRouteSchedulesUseCase;

    @GetMapping("/assets/live")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<PublicAssetStatusDTO>>> getPublicLiveAssets(
            @PageableDefault(size = 50) Pageable pageable) {

        ApiResponseDTO<PagedResponseDTO<PublicAssetStatusDTO>> response = getPublicLiveAssetsUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<PublicRouteDTO>>> listPublicRoutes(
            @PageableDefault(size = 20) Pageable pageable) {

        ApiResponseDTO<PagedResponseDTO<PublicRouteDTO>> response = listPublicRoutesUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/routes/{id}")
    public ResponseEntity<ApiResponseDTO<PublicRouteDTO>> getPublicRouteById(@PathVariable UUID id) {
        ApiResponseDTO<PublicRouteDTO> response = getPublicRouteByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/routes/{id}/schedules")
    public ResponseEntity<ApiResponseDTO<List<PublicScheduleDTO>>> getPublicRouteSchedules(@PathVariable UUID id) {
        ApiResponseDTO<List<PublicScheduleDTO>> response = getPublicRouteSchedulesUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}