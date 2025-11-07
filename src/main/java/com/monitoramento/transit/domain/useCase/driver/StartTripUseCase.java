package com.monitoramento.transit.domain.useCase.driver;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.mapper.TripMapper;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StartTripUseCase {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Transactional
    public ApiResponseDTO<TripResponseDTO> execute(UUID tripId, UserDetailsImpl userDetails) {
        UUID driverId = userDetails.getId();

        Trip trip = tripRepository.findByIdAndDriverId(tripId, driverId)
                .orElse(null);

        if (trip == null) {
            return ApiResponseDTO.empty(404, "Viagem não encontrada ou não pertence a este motorista.");
        }

        if (trip.getStatus() != TripStatus.SCHEDULED) {
            return ApiResponseDTO.error(409, "Esta viagem não pode ser iniciada (Status atual: " + trip.getStatus() + ").");
        }

        trip.setStatus(TripStatus.IN_PROGRESS);
        trip.setActualStartTime(OffsetDateTime.now());

        Trip savedTrip = tripRepository.save(trip);
        return ApiResponseDTO.success(200, "Viagem iniciada", tripMapper.toResponseDTO(savedTrip));
    }
}