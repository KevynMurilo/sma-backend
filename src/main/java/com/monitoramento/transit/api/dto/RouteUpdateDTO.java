package com.monitoramento.transit.api.dto;

import com.monitoramento.transit.domain.model.enums.RouteType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RouteUpdateDTO(
        @NotBlank
        String routeName,
        String routeDescription,
        @NotNull
        RouteType type
) {
}
