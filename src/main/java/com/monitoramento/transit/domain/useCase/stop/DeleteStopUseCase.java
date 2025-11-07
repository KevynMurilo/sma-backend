package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.RouteStopAssignmentRepository;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteStopUseCase {

    private final StopRepository stopRepository;
    private final RouteStopAssignmentRepository routeStopAssignmentRepository;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID stopId) {
        Stop stop = stopRepository.findById(stopId)
                .orElse(null);

        if (stop == null) {
            return ApiResponseDTO.empty(404, "Parada não encontrada");
        }

        // Note: If stop is used in routes, it will be removed due to cascade delete in RouteStopAssignment

        String stopName = stop.getName();
        stopRepository.deleteById(stopId);

        auditService.log(
            AuditAction.DELETE,
            AuditEntity.STOP,
            stopId.toString(),
            "Parada deletada: " + stopName
        );

        return ApiResponseDTO.success(200, "Parada deletada com sucesso");
    }
}
