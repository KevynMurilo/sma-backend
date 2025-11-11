package com.monitoramento.shared.pub.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicRouteDTO;
import com.monitoramento.shared.pub.api.mapper.PublicRouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.user.domain.model.FavoriteRoute;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.infrastructure.persistence.FavoriteRouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListPublicRoutesUseCase {

    private final RouteRepository routeRepository;
    private final PublicRouteMapper publicRouteMapper;
    private final FavoriteRouteRepository favoriteRouteRepository;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<PublicRouteDTO>> execute(Pageable pageable, UserDetailsImpl userDetails) {

        final Set<UUID> favoriteRouteIds;
        if (userDetails != null) {
            favoriteRouteIds = favoriteRouteRepository.findByUserId(userDetails.getId())
                    .stream()
                    .map(FavoriteRoute::getRoute)
                    .map(Route::getId)
                    .collect(Collectors.toSet());
        } else {
            favoriteRouteIds = Collections.emptySet();
        }

        Page<Route> routes = routeRepository.findAll(pageable);

        Page<PublicRouteDTO> dtoPage = routes.map(route -> {
            PublicRouteDTO dto = publicRouteMapper.toPublicDTO(route);
            return new PublicRouteDTO(
                    dto.id(),
                    dto.routeName(),
                    dto.routeDescription(),
                    dto.type(),
                    dto.stops(),
                    favoriteRouteIds.contains(dto.id())
            );
        });

        PagedResponseDTO<PublicRouteDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Rotas encontradas", paged);
    }
}