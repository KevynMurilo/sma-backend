package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListRoutesUseCase {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<RouteResponseDTO>> execute(Pageable pageable) {
        Page<Route> routes = routeRepository.findAll(pageable);
        Page<RouteResponseDTO> dtoPage = routes.map(routeMapper::toResponseDTO);
        PagedResponseDTO<RouteResponseDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Rotas encontradas", paged);
    }
}
