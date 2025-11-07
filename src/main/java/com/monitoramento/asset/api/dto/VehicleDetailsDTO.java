package com.monitoramento.asset.api.dto;

public record VehicleDetailsDTO(
        String licensePlate,
        String model,
        String make,
        int year
) {
}