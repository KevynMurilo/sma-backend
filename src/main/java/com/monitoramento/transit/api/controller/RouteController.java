package com.monitoramento.transit.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.*;
import com.monitoramento.transit.domain.useCase.route.*;
import com.monitoramento.transit.domain.useCase.route.AddStopToRouteUseCase;
import com.monitoramento.transit.domain.useCase.route.CreateRouteUseCase;
import com.monitoramento.transit.domain.useCase.schedule.ListSchedulesByRouteUseCase;
import com.monitoramento.transit.domain.useCase.stop.RemoveStopFromRouteUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/transit/routes")
@RequiredArgsConstructor
public class RouteController {

    private final CreateRouteUseCase createRouteUseCase;
    private final ListRoutesUseCase listRoutesUseCase;
    private final GetRouteByIdUseCase getRouteByIdUseCase;
    private final UpdateRouteUseCase updateRouteUseCase;
    private final DeleteRouteUseCase deleteRouteUseCase;
    private final AddStopToRouteUseCase addStopToRouteUseCase;
    private final RemoveStopFromRouteUseCase removeStopFromRouteUseCase;
    private final ListSchedulesByRouteUseCase listSchedulesByRouteUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<RouteResponseDTO>>> listRoutes(
            @PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<RouteResponseDTO>> response = listRoutesUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RouteResponseDTO>> getRouteById(@PathVariable UUID id) {
        ApiResponseDTO<RouteResponseDTO> response = getRouteByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<RouteResponseDTO>> createRoute(@Valid @RequestBody RouteDTO dto) {
        ApiResponseDTO<RouteResponseDTO> response = createRouteUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RouteResponseDTO>> updateRoute(
            @PathVariable UUID id,
            @Valid @RequestBody RouteUpdateDTO dto) {
        ApiResponseDTO<RouteResponseDTO> response = updateRouteUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteRoute(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteRouteUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{routeId}/stops")
    public ResponseEntity<ApiResponseDTO<RouteResponseDTO>> addStopToRoute(
            @PathVariable UUID routeId,
            @Valid @RequestBody RouteStopAssignmentDTO dto) {

        ApiResponseDTO<RouteResponseDTO> response = addStopToRouteUseCase.execute(routeId, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{routeId}/stops/{stopId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeStopFromRoute(
            @PathVariable UUID routeId,
            @PathVariable UUID stopId) {
        ApiResponseDTO<Void> response = removeStopFromRouteUseCase.execute(routeId, stopId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{routeId}/schedules")
    public ResponseEntity<ApiResponseDTO<List<ScheduleResponseDTO>>> listSchedulesByRoute(
            @PathVariable UUID routeId) {
        ApiResponseDTO<List<ScheduleResponseDTO>> response = listSchedulesByRouteUseCase.execute(routeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}