package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.dto.RouteStopAssignmentDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.domain.model.RouteStopAssignment;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import com.monitoramento.transit.infrastructure.persistence.RouteStopAssignmentRepository;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddStopToRouteUseCase {

    private final RouteRepository routeRepository;
    private final StopRepository stopRepository;
    private final RouteStopAssignmentRepository routeStopAssignmentRepository;
    private final RouteMapper routeMapper;

    @Transactional
    public ApiResponseDTO<RouteResponseDTO> execute(UUID routeId, RouteStopAssignmentDTO dto) {
        Route route = routeRepository.findById(routeId)
                .orElse(null);
        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        Stop stop = stopRepository.findById(dto.stopId())
                .orElse(null);
        if (stop == null) {
            return ApiResponseDTO.empty(404, "Parada não encontrada");
        }

        if (routeStopAssignmentRepository.findByRouteIdAndStopId(routeId, dto.stopId()).isPresent()) {
            return ApiResponseDTO.error(409, "Esta parada já foi adicionada a esta rota.");
        }

        if (routeStopAssignmentRepository.findByRouteIdAndStopOrder(routeId, dto.stopOrder()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe uma parada nesta ordem. Reordene as paradas primeiro.");
        }

        RouteStopAssignment assignment = new RouteStopAssignment();
        assignment.setRoute(route);
        assignment.setStop(stop);
        assignment.setStopOrder(dto.stopOrder());

        routeStopAssignmentRepository.save(assignment);

        Route updatedRoute = routeRepository.findById(routeId).get();
        return ApiResponseDTO.success(201, "Parada adicionada à rota", routeMapper.toResponseDTO(updatedRoute));
    }
}