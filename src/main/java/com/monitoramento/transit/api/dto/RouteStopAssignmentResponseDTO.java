package com.monitoramento.transit.api.dto;

public record RouteStopAssignmentResponseDTO(
        Long id,
        StopResponseDTO stop,
        int stopOrder
) {
}