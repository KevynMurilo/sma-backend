package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteRouteUseCase {

    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElse(null);

        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        if (!scheduleRepository.findByRouteId(routeId).isEmpty()) {
            return ApiResponseDTO.error(409, "Não é possível excluir a rota pois ela possui horários associados.");
        }

        String routeName = route.getRouteName();
        routeRepository.deleteById(routeId);

        auditService.log(
            AuditAction.DELETE,
            AuditEntity.ROUTE,
            routeId.toString(),
            "Rota deletada: " + routeName
        );

        return ApiResponseDTO.success(200, "Rota deletada com sucesso");
    }
}
