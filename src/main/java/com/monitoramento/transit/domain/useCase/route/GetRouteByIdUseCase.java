package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRouteByIdUseCase {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<RouteResponseDTO> execute(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElse(null);

        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        RouteResponseDTO responseDTO = routeMapper.toResponseDTO(route);
        return ApiResponseDTO.success(200, "Rota encontrada", responseDTO);
    }
}
