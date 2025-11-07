package com.monitoramento.organization.domain.useCase.fleet;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.ManagerAssetStatusDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.mapper.ManagerAssetStatusMapper;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import com.monitoramento.tracking.infrastructure.persistence.AssetCurrentStatusRepository;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetManagerFleetStatusUseCase {

    private final AssetCurrentStatusRepository assetCurrentStatusRepository;
    private final ManagerAssetStatusMapper managerAssetStatusMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<ManagerAssetStatusDTO>> execute(UserDetailsImpl userDetails, Pageable pageable) {
        UUID userId = userDetails.getId();

        Page<AssetCurrentStatus> statusPage = assetCurrentStatusRepository.findManagerFleetStatus(userId, pageable);

        Page<ManagerAssetStatusDTO> dtoPage = statusPage.map(managerAssetStatusMapper::toDTO);
        PagedResponseDTO<ManagerAssetStatusDTO> pagedResponse = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Status da frota recuperado com sucesso", pagedResponse);
    }
}