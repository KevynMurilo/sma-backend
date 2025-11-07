package com.monitoramento.user.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.user.domain.model.FavoriteRoute;
import com.monitoramento.user.domain.model.User;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.FavoriteRouteRepository;
import com.monitoramento.user.infrastructure.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddFavoriteRouteUseCase {

    private final FavoriteRouteRepository favoriteRouteRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Transactional
    public ApiResponseDTO<RouteResponseDTO> execute(UUID routeId, UserDetailsImpl userDetails) {
        if (favoriteRouteRepository.existsByUserIdAndRouteId(userDetails.getId(), routeId)) {
            return ApiResponseDTO.error(409, "Esta rota já está nos seus favoritos.");
        }

        User user = userRepository.getReferenceById(userDetails.getId());
        Route route = routeRepository.findById(routeId).orElse(null);

        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada.");
        }

        FavoriteRoute favorite = new FavoriteRoute();
        favorite.setUser(user);
        favorite.setRoute(route);
        favoriteRouteRepository.save(favorite);

        return ApiResponseDTO.success(201, "Rota favoritada com sucesso", routeMapper.toResponseDTO(route));
    }
}