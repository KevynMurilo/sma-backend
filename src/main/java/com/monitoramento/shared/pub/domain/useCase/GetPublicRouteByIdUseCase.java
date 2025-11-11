package com.monitoramento.shared.pub.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicRouteDTO;
import com.monitoramento.shared.pub.api.mapper.PublicRouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.FavoriteRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetPublicRouteByIdUseCase {

    private final RouteRepository routeRepository;
    private final PublicRouteMapper publicRouteMapper;
    private final FavoriteRouteRepository favoriteRouteRepository;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PublicRouteDTO> execute(UUID routeId, UserDetailsImpl userDetails) {
        Route route = routeRepository.findById(routeId)
                .orElse(null);

        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        PublicRouteDTO dto = publicRouteMapper.toPublicDTO(route);

        boolean isFavorite = false;
        if (userDetails != null) {
            isFavorite = favoriteRouteRepository.findByUserId(userDetails.getId())
                    .stream()
                    .anyMatch(fav -> fav.getRoute().getId().equals(routeId));
        }

        PublicRouteDTO responseDto = new PublicRouteDTO(
                dto.id(),
                dto.routeName(),
                dto.routeDescription(),
                dto.type(),
                dto.stops(),
                isFavorite
        );

        return ApiResponseDTO.success(200, "Rota encontrada", responseDto);
    }
}