package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleUpdateDTO;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final RouteRepository routeRepository;
    private final ScheduleMapper scheduleMapper;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<ScheduleResponseDTO> execute(UUID scheduleId, ScheduleUpdateDTO dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return ApiResponseDTO.empty(404, "Horário não encontrado");
        }

        Route route = routeRepository.findById(dto.routeId())
                .orElse(null);

        if (route == null) {
            return ApiResponseDTO.error(404, "Rota não encontrada.");
        }

        if (scheduleRepository.findByRouteIdAndDayProfile(dto.routeId(), dto.dayProfile()).isPresent()
                && (!schedule.getRoute().getId().equals(dto.routeId())
                    || !schedule.getDayProfile().equals(dto.dayProfile()))) {
            return ApiResponseDTO.error(409, "Já existe um horário para esta rota com este perfil de dia.");
        }

        schedule.setRoute(route);
        schedule.setDayProfile(dto.dayProfile());

        Schedule savedSchedule = scheduleRepository.save(schedule);
        ScheduleResponseDTO responseDTO = scheduleMapper.toResponseDTO(savedSchedule);

        auditService.log(
            AuditAction.UPDATE,
            AuditEntity.SCHEDULE,
            scheduleId.toString(),
            "Horário atualizado para rota: " + route.getRouteName()
        );

        return ApiResponseDTO.success(200, "Horário atualizado com sucesso", responseDTO);
    }
}
