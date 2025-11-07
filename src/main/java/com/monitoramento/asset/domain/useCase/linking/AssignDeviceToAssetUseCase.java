package com.monitoramento.asset.domain.useCase.linking;

import com.monitoramento.asset.api.dto.AssignDeviceDTO;
import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.api.mapper.MonitoredAssetMapper;
import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AssignDeviceToAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final TrackingDeviceRepository trackingDeviceRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(UUID assetId, AssignDeviceDTO dto) {
        MonitoredAsset asset = monitoredAssetRepository.findById(assetId)
                .orElse(null);
        if (asset == null) {
            return ApiResponseDTO.empty(404, "Ativo não encontrado");
        }

        TrackingDevice device = trackingDeviceRepository.findById(dto.deviceId())
                .orElse(null);
        if (device == null) {
            return ApiResponseDTO.empty(404, "Dispositivo não encontrado");
        }

        if (asset.getTrackingDevice() != null) {
            return ApiResponseDTO.error(409, "Este ativo já possui um dispositivo vinculado. Desvincule-o primeiro.");
        }

        if (device.getAssignedAsset() != null) {
            return ApiResponseDTO.error(409, "Este dispositivo já está vinculado a outro ativo.");
        }

        asset.setTrackingDevice(device);
        device.setAssignedAsset(asset);
        device.setStatus(DeviceStatus.OFFLINE);

        monitoredAssetRepository.save(asset);
        trackingDeviceRepository.save(device);

        MonitoredAssetResponseDTO responseDTO = monitoredAssetMapper.monitoredAssetToMonitoredAssetResponseDTO(asset);
        return ApiResponseDTO.success(200, "Dispositivo vinculado com sucesso", responseDTO);
    }
}