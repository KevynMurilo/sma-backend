package com.monitoramento.asset.domain.useCase.device;

import com.monitoramento.asset.api.dto.TrackingDeviceUpdateDTO;
import com.monitoramento.asset.api.dto.TrackingDeviceResponseDTO;
import com.monitoramento.asset.api.mapper.TrackingDeviceMapper;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateTrackingDeviceUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final TrackingDeviceMapper trackingDeviceMapper;

    @Transactional
    public ApiResponseDTO<TrackingDeviceResponseDTO> execute(UUID id, TrackingDeviceUpdateDTO dto) {
        TrackingDevice device = trackingDeviceRepository.findById(id)
                .orElse(null);

        if (device == null) {
            return ApiResponseDTO.empty(404, "Dispositivo não encontrado");
        }

        if (device.getAssignedAsset() != null && dto.status() == com.monitoramento.asset.domain.model.enums.DeviceStatus.UNASSIGNED) {
            return ApiResponseDTO.error(409, "Não é possível definir o status como UNASSIGNED pois o dispositivo está vinculado a um ativo.");
        }

        if (device.getAssignedAsset() == null && dto.status() != com.monitoramento.asset.domain.model.enums.DeviceStatus.UNASSIGNED) {
            return ApiResponseDTO.error(409, "Não é possível alterar o status pois o dispositivo não está vinculado a um ativo.");
        }

        device.setModel(dto.model());
        device.setStatus(dto.status());

        TrackingDevice savedDevice = trackingDeviceRepository.save(device);
        TrackingDeviceResponseDTO responseDTO = trackingDeviceMapper.trackingDeviceToTrackingDeviceResponseDTO(savedDevice);

        return ApiResponseDTO.success(200, "Dispositivo atualizado com sucesso", responseDTO);
    }
}