package com.monitoramento.domain.useCase.driver;

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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetMyTripsUseCase {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<List<TripResponseDTO>> execute(UserDetailsImpl userDetails) {
        UUID driverId = userDetails.getId();
        LocalDate today = LocalDate.now();

        List<TripStatus> statuses = Arrays.asList(TripStatus.SCHEDULED, TripStatus.IN_PROGRESS);

        List<Trip> trips = tripRepository.findByDriverAndDateAndStatus(driverId, today, statuses);

        List<TripResponseDTO> dtos = trips.stream()
                .map(tripMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ApiResponseDTO.success(200, "Viagens encontradas", dtos);
    }
}