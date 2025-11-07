package com.monitoramento.transit.api.controller;

import com.monitoramento.transit.domain.useCase.driver.EndTripUseCase;
import com.monitoramento.transit.domain.useCase.driver.GetMyTripsUseCase;
import com.monitoramento.transit.domain.useCase.driver.StartTripUseCase;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/driver")
@RequiredArgsConstructor
public class DriverController {

    private final GetMyTripsUseCase getMyTripsUseCase;
    private final StartTripUseCase startTripUseCase;
    private final EndTripUseCase endTripUseCase;

    @GetMapping("/trips/my-today")
    public ResponseEntity<ApiResponseDTO<List<TripResponseDTO>>> getMyTripsToday(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<List<TripResponseDTO>> response = getMyTripsUseCase.execute(userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/trips/{tripId}/start")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> startTrip(
            @PathVariable UUID tripId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<TripResponseDTO> response = startTripUseCase.execute(tripId, userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/trips/{tripId}/end")
    public ResponseEntity<ApiResponseDTO<TripResponseDTO>> endTrip(
            @PathVariable UUID tripId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<TripResponseDTO> response = endTripUseCase.execute(tripId, userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}