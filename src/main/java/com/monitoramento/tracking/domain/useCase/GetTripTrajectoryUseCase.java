package com.monitoramento.tracking.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.tracking.api.dto.LocationHistoryResponseDTO;
import com.monitoramento.tracking.api.mapper.LocationHistoryMapper;
import com.monitoramento.tracking.domain.model.LocationDataPoint;
import com.monitoramento.tracking.infrastructure.persistence.LocationDataPointRepository;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetTripTrajectoryUseCase {

    private final TripRepository tripRepository;
    private final LocationDataPointRepository locationDataPointRepository;
    private final LocationHistoryMapper locationHistoryMapper;

    public ApiResponseDTO<List<LocationHistoryResponseDTO>> execute(UUID tripId) {
        try {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Trip not found"));

            if (trip.getAsset() == null || trip.getAsset().getTrackingDevice() == null) {
                return ApiResponseDTO.error(404, "Trip has no tracking device assigned");
            }

            OffsetDateTime startTime = trip.getActualStartTime();
            OffsetDateTime endTime = trip.getActualEndTime() != null ? trip.getActualEndTime() : OffsetDateTime.now();

            if (startTime == null) {
                return ApiResponseDTO.error(400, "Trip has not started yet");
            }

            UUID deviceId = trip.getAsset().getTrackingDevice().getId();

            List<LocationDataPoint> trajectory = locationDataPointRepository
                    .findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(deviceId, startTime, endTime);

            List<LocationHistoryResponseDTO> response = trajectory.stream()
                    .map(locationHistoryMapper::toResponseDTO)
                    .toList();

            return ApiResponseDTO.success(200, "Trip trajectory retrieved successfully", response);
        } catch (Exception e) {
            log.error("Error getting trip trajectory for trip {}", tripId, e);
            return ApiResponseDTO.error(500, "Error getting trip trajectory: " + e.getMessage());
        }
    }
}
