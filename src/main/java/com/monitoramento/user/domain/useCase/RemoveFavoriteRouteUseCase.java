package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.user.domain.model.FavoriteRoute;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.FavoriteRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteRouteUseCase {

    private final FavoriteRouteRepository favoriteRouteRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(UUID routeId, UserDetailsImpl userDetails) {
        FavoriteRoute favorite = favoriteRouteRepository.findByUserIdAndRouteId(userDetails.getId(), routeId)
                .orElse(null);

        if (favorite == null) {
            return ApiResponseDTO.empty(404, "Rota favorita não encontrada.");
        }

        favoriteRouteRepository.delete(favorite);
        return ApiResponseDTO.success(200, "Rota removida dos favoritos");
    }
}