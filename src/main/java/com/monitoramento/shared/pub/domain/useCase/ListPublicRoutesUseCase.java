package com.monitoramento.shared.pub.domain.useCase;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.shared.pub.api.dto.PublicRouteDTO;
import com.monitoramento.shared.pub.api.mapper.PublicRouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListPublicRoutesUseCase {

    private final RouteRepository routeRepository;
    private final PublicRouteMapper publicRouteMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<PublicRouteDTO>> execute(Pageable pageable) {
        Page<Route> routes = routeRepository.findAll(pageable);
        Page<PublicRouteDTO> dtoPage = routes.map(publicRouteMapper::toPublicDTO);
        PagedResponseDTO<PublicRouteDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Rotas encontradas", paged);
    }
}
