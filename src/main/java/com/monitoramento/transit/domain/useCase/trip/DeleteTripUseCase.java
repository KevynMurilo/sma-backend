package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTripUseCase {

    private final TripRepository tripRepository;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElse(null);

        if (trip == null) {
            return ApiResponseDTO.empty(404, "Viagem não encontrada");
        }

        String tripInfo = trip.getTripDate().toString();
        tripRepository.deleteById(tripId);

        auditService.log(
            AuditAction.DELETE,
            AuditEntity.TRIP,
            tripId.toString(),
            "Viagem deletada: " + tripInfo
        );

        return ApiResponseDTO.success(200, "Viagem deletada com sucesso");
    }
}
