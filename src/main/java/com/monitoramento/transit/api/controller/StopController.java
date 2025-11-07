package com.monitoramento.transit.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.StopDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.api.dto.StopUpdateDTO;
import com.monitoramento.transit.domain.useCase.stop.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/transit/stops")
@RequiredArgsConstructor
public class StopController {

    private final CreateStopUseCase createStopUseCase;
    private final ListStopsUseCase listStopsUseCase;
    private final GetStopByIdUseCase getStopByIdUseCase;
    private final UpdateStopUseCase updateStopUseCase;
    private final DeleteStopUseCase deleteStopUseCase;
    private final RemoveStopFromRouteUseCase removeStopFromRouteUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<StopResponseDTO>>> listStops(
            @PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<StopResponseDTO>> response = listStopsUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<StopResponseDTO>> getStopById(@PathVariable UUID id) {
        ApiResponseDTO<StopResponseDTO> response = getStopByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<StopResponseDTO>> createStop(@Valid @RequestBody StopDTO dto) {
        ApiResponseDTO<StopResponseDTO> response = createStopUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<StopResponseDTO>> updateStop(
            @PathVariable UUID id,
            @Valid @RequestBody StopUpdateDTO dto) {
        ApiResponseDTO<StopResponseDTO> response = updateStopUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteStop(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteStopUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
