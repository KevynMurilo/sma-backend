package com.monitoramento.transit.api.dto;

import com.monitoramento.asset.api.dto.SimpleDriverDTO;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TripResponseDTO(
        UUID id,
        LocalDate tripDate,
        TripStatus status,
        OffsetDateTime actualStartTime,
        OffsetDateTime actualEndTime,
        SimpleScheduleDepartureDTO scheduleDeparture,
        SimpleAssetDTO asset,
        SimpleDriverDTO driver
) {
}