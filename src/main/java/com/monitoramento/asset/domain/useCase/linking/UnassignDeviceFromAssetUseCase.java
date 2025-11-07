package com.monitoramento.asset.domain.useCase.linking;

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
public class UnassignDeviceFromAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final TrackingDeviceRepository trackingDeviceRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(UUID assetId) {
        MonitoredAsset asset = monitoredAssetRepository.findById(assetId)
                .orElse(null);
        if (asset == null) {
            return ApiResponseDTO.empty(404, "Ativo não encontrado");
        }

        TrackingDevice device = asset.getTrackingDevice();
        if (device == null) {
            return ApiResponseDTO.error(400, "Este ativo não possui dispositivo vinculado.");
        }

        asset.setTrackingDevice(null);
        device.setAssignedAsset(null);
        device.setStatus(DeviceStatus.UNASSIGNED);

        monitoredAssetRepository.save(asset);
        trackingDeviceRepository.save(device);

        MonitoredAssetResponseDTO responseDTO = monitoredAssetMapper.monitoredAssetToMonitoredAssetResponseDTO(asset);
        return ApiResponseDTO.success(200, "Dispositivo desvinculado com sucesso", responseDTO);
    }
}