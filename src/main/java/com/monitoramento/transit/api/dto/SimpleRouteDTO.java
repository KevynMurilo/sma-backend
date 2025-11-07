package com.monitoramento.transit.api.dto;

import java.util.UUID;

public record SimpleRouteDTO(
        UUID id,
        String routeName
) {}