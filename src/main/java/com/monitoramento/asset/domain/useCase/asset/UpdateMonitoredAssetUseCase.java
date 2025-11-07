package com.monitoramento.asset.domain.useCase.asset;

import com.monitoramento.asset.api.dto.MonitoredAssetUpdateDTO;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateMonitoredAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final FleetRepository fleetRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;

    @Transactional
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(UUID assetId, MonitoredAssetUpdateDTO dto) {
        MonitoredAsset asset = monitoredAssetRepository.findById(assetId)
                .orElse(null);

        if (asset == null) {
            return ApiResponseDTO.empty(404, "Ativo não encontrado");
        }

        Fleet fleet = fleetRepository.findById(dto.fleetId())
                .orElse(null);

        if (fleet == null) {
            return ApiResponseDTO.error(404, "Frota não encontrada.");
        }

        asset.setName(dto.name());
        asset.setType(dto.type());
        asset.setPubliclyVisible(dto.isPubliclyVisible());
        asset.setFleet(fleet);

        MonitoredAsset savedAsset = monitoredAssetRepository.save(asset);

        VehicleDetails details = vehicleDetailsRepository.findById(assetId)
                .orElse(null);

        boolean hasDetailsInDto = dto.licensePlate() != null || dto.model() != null;

        if (details == null && hasDetailsInDto) {
            details = new VehicleDetails();
            details.setAsset(savedAsset);
        }

        if (details != null) {
            details.setLicensePlate(dto.licensePlate());
            details.setMake(dto.make());
            details.setModel(dto.model());
            if (dto.year() != null) {
                details.setYear(dto.year());
            }
            vehicleDetailsRepository.save(details);
        }

        MonitoredAssetResponseDTO responseDTO = monitoredAssetMapper.monitoredAssetToMonitoredAssetResponseDTO(savedAsset);
        return ApiResponseDTO.success(200, "Ativo atualizado com sucesso", responseDTO);
    }
}