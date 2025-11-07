package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.TripCreateDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
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

@Service
@RequiredArgsConstructor
public class CreateTripUseCase {

    private final TripRepository tripRepository;
    private final ScheduleDepartureRepository scheduleDepartureRepository;
    private final MonitoredAssetRepository monitoredAssetRepository;
    private final UserRepository userRepository;
    private final TripMapper tripMapper;

    @Transactional
    public ApiResponseDTO<TripResponseDTO> execute(TripCreateDTO dto) {
        ScheduleDeparture departure = scheduleDepartureRepository.findById(dto.scheduleDepartureId())
                .orElse(null);
        if (departure == null) {
            return ApiResponseDTO.empty(404, "Horário de partida não encontrado.");
        }

        MonitoredAsset asset = monitoredAssetRepository.findById(dto.assetId())
                .orElse(null);
        if (asset == null) {
            return ApiResponseDTO.empty(404, "Ativo (Veículo) não encontrado.");
        }

        User driver = userRepository.findById(dto.driverId())
                .orElse(null);
        if (driver == null) {
            return ApiResponseDTO.empty(404, "Motorista não encontrado.");
        }

        Trip trip = new Trip();
        trip.setScheduleDeparture(departure);
        trip.setTripDate(dto.tripDate());
        trip.setAsset(asset);
        trip.setDriver(driver);
        trip.setStatus(com.monitoramento.transit.domain.model.enums.TripStatus.SCHEDULED);

        Trip savedTrip = tripRepository.save(trip);

        Trip fullTrip = tripRepository.findById(savedTrip.getId()).get();
        return ApiResponseDTO.success(201, "Viagem criada e alocada com sucesso", tripMapper.toResponseDTO(fullTrip));
    }
}