package com.monitoramento.shared.manager.domain.useCase;

import com.monitoramento.asset.api.dto.MonitoredAssetCreateDTO;
import com.monitoramento.asset.api.dto.MonitoredAssetResponseDTO;
import com.monitoramento.asset.api.mapper.MonitoredAssetMapper;
import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.asset.domain.model.VehicleDetails;
import com.monitoramento.asset.infrastructure.persistence.MonitoredAssetRepository;
import com.monitoramento.asset.infrastructure.persistence.VehicleDetailsRepository;
import com.monitoramento.organization.domain.model.Department;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.organization.infrastructure.persistence.FleetRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManagerCreateAssetUseCase {

    private final MonitoredAssetRepository monitoredAssetRepository;
    private final FleetRepository fleetRepository;
    private final VehicleDetailsRepository vehicleDetailsRepository;
    private final MonitoredAssetMapper monitoredAssetMapper;
    private final UserRepository userRepository;

    @Transactional
    public ApiResponseDTO<MonitoredAssetResponseDTO> execute(MonitoredAssetCreateDTO dto, UserDetailsImpl userDetails) {
        Fleet fleet = fleetRepository.findById(dto.fleetId())
                .orElse(null);

        if (fleet == null) {
            return ApiResponseDTO.error(404, "Frota não encontrada.");
        }

        // VERIFICAÇÃO DE PERMISSÃO
        User manager = userRepository.findById(userDetails.getId()).get();
        Set<UUID> manageableDeptIds = manager.getManageableDepartments().stream()
                .map(Department::getId)
                .collect(Collectors.toSet());

        if (!manageableDeptIds.contains(fleet.getDepartment().getId())) {
            return ApiResponseDTO.error(403, "Acesso negado. Você não tem permissão para esta frota.");
        }

        // Lógica de criação (duplicada de CreateMonitoredAssetUseCase, agora com escopo)
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