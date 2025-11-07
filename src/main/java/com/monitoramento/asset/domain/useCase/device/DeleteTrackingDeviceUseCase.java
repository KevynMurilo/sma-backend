package com.monitoramento.asset.domain.useCase.device;

import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteTrackingDeviceUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID id) {
        TrackingDevice device = trackingDeviceRepository.findById(id)
                .orElse(null);

        if (device == null) {
            return ApiResponseDTO.empty(404, "Dispositivo não encontrado");
        }

        if (device.getAssignedAsset() != null) {
            return ApiResponseDTO.error(409, "Não é possível excluir o dispositivo pois ele está vinculado a um ativo. Desvincule-o primeiro.");
        }

        trackingDeviceRepository.delete(device);
        return ApiResponseDTO.success(200, "Dispositivo deletado com sucesso");
    }
}