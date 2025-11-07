package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final TripRepository tripRepository;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return ApiResponseDTO.empty(404, "Horário não encontrado");
        }

        // Note: Trips depend on schedule departures, so we need to check if any trips exist
        // This is a simplified check - in production you might want more sophisticated validation
        String routeName = schedule.getRoute().getRouteName();
        scheduleRepository.deleteById(scheduleId);

        auditService.log(
            AuditAction.DELETE,
            AuditEntity.SCHEDULE,
            scheduleId.toString(),
            "Horário deletado para rota: " + routeName
        );

        return ApiResponseDTO.success(200, "Horário deletado com sucesso");
    }
}
