package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateRouteUseCase {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Transactional
    public ApiResponseDTO<RouteResponseDTO> execute(RouteDTO dto) {
        if (routeRepository.findByRouteName(dto.routeName()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe uma rota com este nome.");
        }

        Route route = routeMapper.toEntity(dto);
        Route savedRoute = routeRepository.save(route);
        return ApiResponseDTO.success(201, "Rota criada com sucesso", routeMapper.toResponseDTO(savedRoute));
    }
}