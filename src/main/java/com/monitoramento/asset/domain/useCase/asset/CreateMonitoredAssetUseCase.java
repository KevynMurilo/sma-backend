package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.api.dto.MonitoredAssetCreateDTO;
import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.api.mapper.MonitoredAssetMapper;
import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.VehicleDetails;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.VehicleDetailsRepository;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateMonitoredAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final FleetRepository fleetRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(MonitoredAssetCreateDTO dto) {
        Fleet fleet = fleetRepository.findById(dto.fleetId())
                .orElse(null);

        if (fleet == null) {
            return ApiResponseDTO.error(404, "Frota não encontrada.");
        }

        MonitoredAsset asset = monitoredAssetMapper.assetCreateDTOToMonitoredAsset(dto, fleet);
        MonitoredAsset savedAsset = monitoredAssetRepository.save(asset);

        if (dto.licensePlate() != null || dto.model() != null) {
            VehicleDetails details = monitoredAssetMapper.assetCreateDTOToVehicleDetails(dto);
            details.setAsset(savedAsset);
            vehicleDetailsRepository.save(details);
        }

        MonitoredAssetResponseDTO responseDTO = monitoredAssetMapper.monitoredAssetToMonitoredAssetResponseDTO(savedAsset);
        return ApiResponseDTO.success(201, "Ativo criado com sucesso", responseDTO);
    }
}