package com.monitoramento.asset.api.controller;

import com.monitoramento.asset.api.dto.TrackingDeviceCreateDTO;
import com.monitoramento.asset.api.dto.TrackingDeviceResponseDTO;
import com.monitoramento.asset.api.dto.TrackingDeviceUpdateDTO;
import com.monitoramento.asset.domain.useCase.device.*;
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
@RequestMapping("/api/v1/admin/devices")
@RequiredArgsConstructor
public class TrackingDeviceController {

    private final CreateTrackingDeviceUseCase createTrackingDeviceUseCase;
    private final UpdateTrackingDeviceUseCase updateTrackingDeviceUseCase;
    private final DeleteTrackingDeviceUseCase deleteTrackingDeviceUseCase;
    private final ListTrackingDevicesUseCase listTrackingDevicesUseCase;
    private final ListUnassignedDevicesUseCase listUnassignedDevicesUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TrackingDeviceResponseDTO>> createDevice(@Valid @RequestBody TrackingDeviceCreateDTO dto) {
        ApiResponseDTO<TrackingDeviceResponseDTO> response = createTrackingDeviceUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TrackingDeviceResponseDTO>> updateDevice(@PathVariable UUID id, @Valid @RequestBody TrackingDeviceUpdateDTO dto) {
        ApiResponseDTO<TrackingDeviceResponseDTO> response = updateTrackingDeviceUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteDevice(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteTrackingDeviceUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<TrackingDeviceResponseDTO>>> listDevices(@PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<TrackingDeviceResponseDTO>> response = listTrackingDevicesUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<TrackingDeviceResponseDTO>>> listUnassignedDevices(@PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<TrackingDeviceResponseDTO>> response = listUnassignedDevicesUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}