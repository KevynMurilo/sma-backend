package com.monitoramento.shared.pub.api;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.dto.PublicAssetStatusDTO;
import com.monitoramento.shared.pub.domain.useCase.GetPublicLiveAssetsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicController {

    private final GetPublicLiveAssetsUseCase getPublicLiveAssetsUseCase;

    @GetMapping("/assets/live")
    public ResponseEntity<ApiResponseDTO<PagedResponseDTO<PublicAssetStatusDTO>>> getPublicLiveAssets(
            @PageableDefault(size = 50) Pageable pageable) {

        ApiResponseDTO<PagedResponseDTO<PublicAssetStatusDTO>> response = getPublicLiveAssetsUseCase.execute(pageable);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}