package com.monitoramento.shared.pub.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicScheduleDTO;
import com.monitoramento.shared.pub.api.mapper.PublicScheduleMapper;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetPublicRouteSchedulesUseCase {

    private final ScheduleRepository scheduleRepository;
    private final PublicScheduleMapper publicScheduleMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<List<PublicScheduleDTO>> execute(UUID routeId) {
        List<Schedule> schedules = scheduleRepository.findByRouteId(routeId);

        List<PublicScheduleDTO> dtos = schedules.stream()
                .map(publicScheduleMapper::toPublicDTO)
                .collect(Collectors.toList());

        return ApiResponseDTO.success(200, "Horários da rota encontrados", dtos);
    }
}
