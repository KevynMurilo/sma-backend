package com.monitoramento.asset.api.controller;

import com.monitoramento.asset.api.dto.*;
import com.monitoramento.asset.domain.useCase.asset.*;
import com.monitoramento.asset.domain.useCase.linking.AssignDeviceToAssetUseCase;
import com.monitoramento.asset.domain.useCase.linking.UnassignDeviceFromAssetUseCase;
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
@RequestMapping("/api/v1/admin/assets")
@RequiredArgsConstructor
public class MonitoredAssetController {

    private final CreateMonitoredAssetUseCase createMonitoredAssetUseCase;
    private final UpdateMonitoredAssetUseCase updateMonitoredAssetUseCase;
    private final DeleteMonitoredAssetUseCase deleteMonitoredAssetUseCase;
    private final GetMonitoredAssetByIdUseCase getMonitoredAssetByIdUseCase;
    private final ListMonitoredAssetsByFleetUseCase listMonitoredAssetsByFleetUseCase;
    private final AssignDeviceToAssetUseCase assignDeviceToAssetUseCase;
    private final UnassignDeviceFromAssetUseCase unassignDeviceFromAssetUseCase;
    private final GetVehicleDetailsByAssetIdUseCase getVehicleDetailsByAssetIdUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> createAsset(@Valid @RequestBody MonitoredAssetCreateDTO dto) {
        ApiResponseDTO<MonitoredAssetResponseDTO> response = createMonitoredAssetUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> updateAsset(@PathVariable UUID id, @Valid @RequestBody MonitoredAssetUpdateDTO dto) {
        ApiResponseDTO<MonitoredAssetResponseDTO> response = updateMonitoredAssetUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteAsset(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteMonitoredAssetUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> getAssetById(@PathVariable UUID id) {
        ApiResponseDTO<MonitoredAssetResponseDTO> response = getMonitoredAssetByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}/vehicle-details")
    public ResponseEntity<ApiResponseDTO<VehicleDetailsDTO>> getVehicleDetails(@PathVariable UUID id) {
        ApiResponseDTO<VehicleDetailsDTO> response = getVehicleDetailsByAssetIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/by-fleet/{fleetId}")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<MonitoredAssetResponseDTO>>> listAssetsByFleet(
            @PathVariable UUID fleetId,
            @PageableDefault(size = 20) Pageable pageable) {

        ApiResponseDTO<PagedResponseDTO<MonitoredAssetResponseDTO>> response = listMonitoredAssetsByFleetUseCase.execute(fleetId, pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{assetId}/assign-device")
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> assignDevice(
            @PathVariable UUID assetId,
            @Valid @RequestBody AssignDeviceDTO dto) {

        ApiResponseDTO<MonitoredAssetResponseDTO> response = assignDeviceToAssetUseCase.execute(assetId, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{assetId}/unassign-device")
    public ResponseEntity<ApiResponseDTO<MonitoredAssetResponseDTO>> unassignDevice(@PathVariable UUID assetId) {
        ApiResponseDTO<MonitoredAssetResponseDTO> response = unassignDeviceFromAssetUseCase.execute(assetId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}