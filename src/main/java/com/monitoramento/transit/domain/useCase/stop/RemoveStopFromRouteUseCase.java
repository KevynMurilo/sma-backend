package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.domain.model.RouteStopAssignment;
import com.monitoramento.transit.infrastructure.persistence.RouteStopAssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveStopFromRouteUseCase {

    private final RouteStopAssignmentRepository routeStopAssignmentRepository;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID routeId, UUID stopId) {
        RouteStopAssignment assignment = routeStopAssignmentRepository.findByRouteIdAndStopId(routeId, stopId)
                .orElse(null);

        if (assignment == null) {
            return ApiResponseDTO.empty(404, "Associação entre rota e parada não encontrada");
        }

        routeStopAssignmentRepository.delete(assignment);

        auditService.log(
            AuditAction.DELETE,
            AuditEntity.ROUTE,
            routeId.toString(),
            "Parada removida da rota: " + stopId
        );

        return ApiResponseDTO.success(200, "Parada removida da rota com sucesso");
    }
}
