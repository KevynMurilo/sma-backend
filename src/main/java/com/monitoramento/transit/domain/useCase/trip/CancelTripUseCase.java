package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.mapper.TripMapper;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelTripUseCase {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<TripResponseDTO> execute(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElse(null);

        if (trip == null) {
            return ApiResponseDTO.empty(404, "Viagem não encontrada");
        }

        if (trip.getStatus() == TripStatus.COMPLETED) {
            return ApiResponseDTO.error(409, "Não é possível cancelar uma viagem já concluída.");
        }

        if (trip.getStatus() == TripStatus.CANCELLED) {
            return ApiResponseDTO.error(409, "Viagem já está cancelada.");
        }

        trip.setStatus(TripStatus.CANCELLED);

        Trip savedTrip = tripRepository.save(trip);
        TripResponseDTO responseDTO = tripMapper.toResponseDTO(savedTrip);

        auditService.log(
            AuditAction.UPDATE,
            AuditEntity.TRIP,
            tripId.toString(),
            "Viagem cancelada"
        );

        return ApiResponseDTO.success(200, "Viagem cancelada com sucesso", responseDTO);
    }
}
