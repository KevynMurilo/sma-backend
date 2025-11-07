package com.monitoramento.shared.pub.api.mapper;

import com.monitoramento.shared.pub.api.dto.PublicRouteDTO;
import com.monitoramento.shared.pub.api.dto.PublicStopDTO;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.domain.model.RouteStopAssignment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublicRouteMapper {

    public PublicRouteDTO toPublicDTO(Route route) {
        List<PublicStopDTO> stops = route.getStops() != null
                ? route.getStops().stream()
                    .map(this::toPublicStopDTO)
                    .collect(Collectors.toList())
                : List.of();

        return new PublicRouteDTO(
                route.getId(),
                route.getRouteName(),
                route.getRouteDescription(),
                route.getType(),
                stops
        );
    }

    private PublicStopDTO toPublicStopDTO(RouteStopAssignment assignment) {
        return new PublicStopDTO(
                assignment.getStop().getId(),
                assignment.getStop().getName(),
                assignment.getStop().getDescription(),
                assignment.getStop().getLatitude(),
                assignment.getStop().getLongitude(),
                assignment.getStopOrder()
        );
    }
}
