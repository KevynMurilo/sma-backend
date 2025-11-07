package com.monitoramento.organization.api.controller;

import com.monitoramento.organization.api.dto.FleetCreateDTO;
import com.monitoramento.organization.api.dto.FleetResponseDTO;
import com.monitoramento.organization.api.dto.FleetUpdateDTO;
import com.monitoramento.organization.domain.useCase.fleet.*;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/fleets")
@RequiredArgsConstructor
public class FleetController {

    private final CreateFleetUseCase createFleetUseCase;
    private final UpdateFleetUseCase updateFleetUseCase;
    private final DeleteFleetUseCase deleteFleetUseCase;
    private final GetFleetByIdUseCase getFleetByIdUseCase;
    private final ListFleetsByDepartmentUseCase listFleetsByDepartmentUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FleetResponseDTO>> createFleet(@Valid @RequestBody FleetCreateDTO dto) {
        ApiResponseDTO<FleetResponseDTO> response = createFleetUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FleetResponseDTO>> updateFleet(@PathVariable UUID id, @Valid @RequestBody FleetUpdateDTO dto) {
        ApiResponseDTO<FleetResponseDTO> response = updateFleetUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteFleet(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteFleetUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FleetResponseDTO>> getFleetById(@PathVariable UUID id) {
        ApiResponseDTO<FleetResponseDTO> response = getFleetByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<FleetResponseDTO>>> listFleetsByDepartment(
            @PathVariable UUID departmentId,
            @PageableDefault(size = 20) Pageable pageable) {

        ApiResponseDTO<PagedResponseDTO<FleetResponseDTO>> response = listFleetsByDepartmentUseCase.execute(departmentId, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}