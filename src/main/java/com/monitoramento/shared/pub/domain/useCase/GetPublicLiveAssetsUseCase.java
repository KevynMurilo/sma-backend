package com.monitoramento.shared.pub.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicAssetStatusDTO;
import com.monitoramento.shared.pub.api.mapper.PublicAssetStatusMapper;
import com.monitoramento.tracking.domain.model.AssetCurrentStatus;
import com.monitoramento.tracking.infrastructure.persistence.AssetCurrentStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPublicLiveAssetsUseCase {

    private final AssetCurrentStatusRepository assetCurrentStatusRepository;
    private final PublicAssetStatusMapper publicAssetStatusMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<PublicAssetStatusDTO>> execute(Pageable pageable) {
        Page<AssetCurrentStatus> statusPage = assetCurrentStatusRepository.findPublicLiveAssets(pageable);

        Page<PublicAssetStatusDTO> dtoPage = statusPage.map(publicAssetStatusMapper::toDTO);
        PagedResponseDTO<PublicAssetStatusDTO> pagedResponse = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Ativos públicos recuperados com sucesso", pagedResponse);
    }
}