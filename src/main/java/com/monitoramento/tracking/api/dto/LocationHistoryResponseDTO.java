package com.monitoramento.tracking.api.dto;

import java.time.OffsetDateTime;

public record LocationHistoryResponseDTO(
        Long id,
        Double latitude,
        Double longitude,
        OffsetDateTime timestamp,
        Double speed,
        Double heading,
        Double altitude
) {}
