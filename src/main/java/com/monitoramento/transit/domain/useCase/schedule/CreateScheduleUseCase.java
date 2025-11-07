package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional
    public ApiResponseDTO<ScheduleResponseDTO> execute(ScheduleDTO dto) {
        Route route = routeRepository.findById(dto.routeId())
                .orElse(null);
        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        if (scheduleRepository.findByRouteIdAndDayProfile(dto.routeId(), dto.dayProfile()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe um agendamento para esta rota neste perfil de dia.");
        }

        Schedule schedule = scheduleMapper.toEntity(dto, route);
        Schedule savedSchedule = scheduleRepository.save(schedule);

        return ApiResponseDTO.success(201, "Agendamento criado", scheduleMapper.toResponseDTO(savedSchedule));
    }
}