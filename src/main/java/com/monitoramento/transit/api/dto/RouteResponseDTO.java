package com.monitoramento.transit.api.dto;

import com.monitoramento.transit.domain.model.enums.RouteType;
import java.util.List;
import java.util.UUID;

public record RouteResponseDTO(
        UUID id,
        String routeName,
        String routeDescription,
        RouteType type,
        List<RouteStopAssignmentResponseDTO> stops
) {
}