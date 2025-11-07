package com.monitoramento.tracking.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.tracking.api.dto.LocationHistoryResponseDTO;
import com.monitoramento.tracking.domain.useCase.GetLocationHistoryUseCase;
import com.monitoramento.tracking.domain.useCase.GetTripTrajectoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class LocationHistoryController {

    private final GetLocationHistoryUseCase getLocationHistoryUseCase;
    private final GetTripTrajectoryUseCase getTripTrajectoryUseCase;

    @GetMapping("/assets/{assetId}/history")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<LocationHistoryResponseDTO>>> getAssetHistory(
            @PathVariable UUID assetId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        ApiResponseDTO<PagedResponseDTO<LocationHistoryResponseDTO>> response =
                getLocationHistoryUseCase.execute(assetId, startDate, endDate, pageable);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/trips/{tripId}/trajectory")
    public ResponseEntity<ApiResponseDTO<List<LocationHistoryResponseDTO>>> getTripTrajectory(
            @PathVariable UUID tripId) {

        ApiResponseDTO<List<LocationHistoryResponseDTO>> response = getTripTrajectoryUseCase.execute(tripId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
