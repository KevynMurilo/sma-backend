package com.monitoramento.transit.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleDTO;
import com.monitoramento.transit.api.dto.ScheduleDepartureDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleUpdateDTO;
import com.monitoramento.transit.domain.useCase.schedule.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/transit/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final CreateScheduleUseCase createScheduleUseCase;
    private final ListSchedulesUseCase listSchedulesUseCase;
    private final GetScheduleByIdUseCase getScheduleByIdUseCase;
    private final UpdateScheduleUseCase updateScheduleUseCase;
    private final DeleteScheduleUseCase deleteScheduleUseCase;
    private final AddDepartureToScheduleUseCase addDepartureToScheduleUseCase;
    private final RemoveDepartureFromScheduleUseCase removeDepartureFromScheduleUseCase;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<ScheduleResponseDTO>>> listSchedules(
            @PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDTO<PagedResponseDTO<ScheduleResponseDTO>> response = listSchedulesUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ScheduleResponseDTO>> getScheduleById(@PathVariable UUID id) {
        ApiResponseDTO<ScheduleResponseDTO> response = getScheduleByIdUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponseDTO<ScheduleResponseDTO>> createSchedule(@Valid @RequestBody ScheduleDTO dto) {
        ApiResponseDTO<ScheduleResponseDTO> response = createScheduleUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ScheduleResponseDTO>> updateSchedule(
            @PathVariable UUID id,
            @Valid @RequestBody ScheduleUpdateDTO dto) {
        ApiResponseDTO<ScheduleResponseDTO> response = updateScheduleUseCase.execute(id, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteSchedule(@PathVariable UUID id) {
        ApiResponseDTO<Void> response = deleteScheduleUseCase.execute(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/{scheduleId}/departures")
    public ResponseEntity<ApiResponseDTO<ScheduleResponseDTO>> addDeparture(
            @PathVariable UUID scheduleId,
            @Valid @RequestBody ScheduleDepartureDTO dto) {

        ApiResponseDTO<ScheduleResponseDTO> response = addDepartureToScheduleUseCase.execute(scheduleId, dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/departures/{departureId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeDeparture(@PathVariable Long departureId) {
        ApiResponseDTO<Void> response = removeDepartureFromScheduleUseCase.execute(departureId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
