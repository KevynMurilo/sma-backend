package com.monitoramento.tracking.domain.service;

import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.tracking.api.dto.IngestionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IngestionProcessingService {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void processIngestion(IngestionRequest request) {
        TrackingDevice device = trackingDeviceRepository.findByDeviceSerial(request.deviceSerial())
                .orElse(null);

        if (device == null) {
            log.warn("Recebida requisição de ingestão para dispositivo desconhecido: {}", request.deviceSerial());
            return;
        }

        if (device.getAssignedAsset() == null) {
            log.warn("Dispositivo {} não está associado a nenhum ativo.", request.deviceSerial());
            return;
        }

        eventPublisher.publishEvent(request);
    }
}