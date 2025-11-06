package com.monitoramento.organization.api.controller;

import com.monitoramento.organization.api.dto.FleetDTO;
import com.monitoramento.organization.domain.useCase.fleet.CreateFleetUseCase;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fleet")
@RequiredArgsConstructor
public class FleetController {

    private final CreateFleetUseCase createFleetUseCase;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FleetDTO>> createFleet(@RequestBody FleetDTO dto) {
        ApiResponseDTO<FleetDTO> response = createFleetUseCase.execute(dto);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
