package com.monitoramento.transit.api.dto;

import java.time.LocalTime;

public record SimpleScheduleDepartureDTO(
        Long id,
        LocalTime departureTime,
        SimpleRouteDTO route
) {}