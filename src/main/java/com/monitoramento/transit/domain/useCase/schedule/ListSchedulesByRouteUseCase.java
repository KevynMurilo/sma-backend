package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListSchedulesByRouteUseCase {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<List<ScheduleResponseDTO>> execute(UUID routeId) {
        List<Schedule> schedules = scheduleRepository.findByRouteId(routeId);
        List<ScheduleResponseDTO> dtos = schedules.stream()
                .map(scheduleMapper::toResponseDTO)
                .toList();

        return ApiResponseDTO.success(200, "Horários da rota encontrados", dtos);
    }
}
