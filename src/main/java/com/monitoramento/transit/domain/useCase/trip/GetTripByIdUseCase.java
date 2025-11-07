package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.mapper.TripMapper;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetTripByIdUseCase {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<TripResponseDTO> execute(UUID tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElse(null);

        if (trip == null) {
            return ApiResponseDTO.empty(404, "Viagem não encontrada");
        }

        TripResponseDTO responseDTO = tripMapper.toResponseDTO(trip);
        return ApiResponseDTO.success(200, "Viagem encontrada", responseDTO);
    }
}
