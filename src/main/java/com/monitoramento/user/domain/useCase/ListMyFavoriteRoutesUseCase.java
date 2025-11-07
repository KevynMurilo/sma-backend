package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.user.domain.model.FavoriteRoute;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.FavoriteRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListMyFavoriteRoutesUseCase {

    private final FavoriteRouteRepository favoriteRouteRepository;
    private final RouteMapper routeMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<List<RouteResponseDTO>> execute(UserDetailsImpl userDetails) {
        List<RouteResponseDTO> favorites = favoriteRouteRepository.findByUserId(userDetails.getId())
                .stream()
                .map(FavoriteRoute::getRoute)
                .map(routeMapper::toResponseDTO)
                .collect(Collectors.toList());

        return ApiResponseDTO.success(200, "Rotas favoritas encontradas", favorites);
    }
}