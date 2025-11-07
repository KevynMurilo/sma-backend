package com.monitoramento.shared.pub.api.dto;

import com.monitoramento.transit.domain.model.enums.RouteType;

import java.util.List;
import java.util.UUID;

public record PublicRouteDTO(
        UUID id,
        String routeName,
        String routeDescription,
        RouteType type,
        List<PublicStopDTO> stops
) {
}
