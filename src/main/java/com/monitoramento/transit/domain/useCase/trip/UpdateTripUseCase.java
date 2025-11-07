package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.dto.TripUpdateDTO;
import com.monitoramento.transit.api.mapper.TripMapper;
import com.monitoramento.transit.domain.model.ScheduleDeparture;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.infrastructure.persistence.ScheduleDepartureRepository;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTripUseCase {

    private final TripRepository tripRepository;
    private final ScheduleDepartureRepository scheduleDepartureRepository;
    private final MonitoredAssetRepository monitoredAssetRepository;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<TripResponseDTO> execute(UUID tripId, TripUpdateDTO dto) {
        Trip trip = tripRepository.findById(tripId)
                .orElse(null);

        if (trip == null) {
            return ApiResponseDTO.empty(404, "Viagem não encontrada");
        }

        ScheduleDeparture scheduleDeparture = scheduleDepartureRepository.findById(dto.scheduleDepartureId())
                .orElse(null);

        if (scheduleDeparture == null) {
            return ApiResponseDTO.error(404, "Horário de partida não encontrado.");
        }

        MonitoredAsset asset = monitoredAssetRepository.findById(dto.assetId())
                .orElse(null);

        if (asset == null) {
            return ApiResponseDTO.error(404, "Ativo não encontrado.");
        }

        User driver = userRepository.findById(dto.driverId())
                .orElse(null);

        if (driver == null) {
            return ApiResponseDTO.error(404, "Motorista não encontrado.");
        }

        trip.setScheduleDeparture(scheduleDeparture);
        trip.setTripDate(dto.tripDate());
        trip.setAsset(asset);
        trip.setDriver(driver);

        Trip savedTrip = tripRepository.save(trip);
        TripResponseDTO responseDTO = tripMapper.toResponseDTO(savedTrip);

        auditService.log(
            AuditAction.UPDATE,
            AuditEntity.TRIP,
            tripId.toString(),
            "Viagem atualizada para data: " + dto.tripDate()
        );

        return ApiResponseDTO.success(200, "Viagem atualizada com sucesso", responseDTO);
    }
}
