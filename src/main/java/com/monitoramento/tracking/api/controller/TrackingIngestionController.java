package com.monitoramento.tracking.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.tracking.api.dto.IngestionRequest;
import com.monitoramento.tracking.domain.service.IngestionProcessingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tracking")
@RequiredArgsConstructor
public class TrackingIngestionController {

    private final IngestionProcessingService ingestionProcessingService;

    @PostMapping("/ingest")
    public ResponseEntity<ApiResponseDTO<Void>> ingest(@Valid @RequestBody IngestionRequest request) {

        ingestionProcessingService.processIngestion(request);

        return ResponseEntity.status(202)
                .body(ApiResponseDTO.success(202, "Dados recebidos para processamento."));
    }
}