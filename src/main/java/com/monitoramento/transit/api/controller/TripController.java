package com.monitoramento.transit.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.TripCreateDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.dto.TripUpdateDTO;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.transit.domain.useCase.trip.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/transit/trips")
@RequiredArgsConstructor
public class TripController {

    private final CreateTripUseCase createTripUseCase;
    private final ListTripsUseCase listTripsUseCase;
    private final GetTripByIdUseCase getTripByIdUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final CancelTripUseCase cancelTripUseCase;
    private final DeleteTripUseCase deleteTripUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<TripResponseDTO>>> listTrips(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) TripStatus status,
            @RequestParam(required = false) UUID assetId,
            @RequestParam(required = false) UUID driverId) {

        ApiResponseDTO<PagedResponseDTO<TripResponseDTO>> response =
                listTripsUseCase.execute(pageable, date, status, assetId, driverId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> getTripById(@PathVariable UUID id) {
        ApiResponseDTO<TripResponseDTO> response = getTripByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> createTrip(@Valid @RequestBody TripCreateDTO dto) {
        ApiResponseDTO<TripResponseDTO> response = createTripUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> updateTrip(
            @PathVariable UUID id,
            @Valid @RequestBody TripUpdateDTO dto) {
        ApiResponseDTO<TripResponseDTO> response = updateTripUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> cancelTrip(@PathVariable UUID id) {
        ApiResponseDTO<TripResponseDTO> response = cancelTripUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteTrip(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteTripUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}