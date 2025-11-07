package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.tracking.infrastructure.persistence.AssetCurrentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMonitoredAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final TrackingDeviceRepository trackingDeviceRepository;
    private final AssetCurrentStatusRepository assetCurrentStatusRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID assetId) {
        MonitoredAsset asset = monitoredAssetRepository.findById(assetId)
                .orElse(null);

        if (asset == null) {
            return ApiResponseDTO.empty(404, "Ativo não encontrado");
        }

        if (asset.getTrackingDevice() != null) {
            TrackingDevice device = asset.getTrackingDevice();
            device.setAssignedAsset(null);
            device.setStatus(com.monitoramento.asset.domain.model.enums.DeviceStatus.UNASSIGNED);
            trackingDeviceRepository.save(device);
        }

        if (assetCurrentStatusRepository.existsById(assetId)) {
            assetCurrentStatusRepository.deleteById(assetId);
        }

        monitoredAssetRepository.delete(asset);

        return ApiResponseDTO.success(200, "Ativo deletado com sucesso");
    }
}