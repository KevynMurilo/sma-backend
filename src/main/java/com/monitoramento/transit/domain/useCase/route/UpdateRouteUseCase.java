package com.monitoramento.transit.domain.useCase.route;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.transit.api.dto.RouteUpdateDTO;
import com.monitoramento.transit.api.mapper.RouteMapper;
import com.monitoramento.transit.domain.model.Route;
import com.monitoramento.transit.infrastructure.persistence.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateRouteUseCase {

    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<RouteResponseDTO> execute(UUID routeId, RouteUpdateDTO dto) {
        Route route = routeRepository.findById(routeId)
                .orElse(null);

        if (route == null) {
            return ApiResponseDTO.empty(404, "Rota não encontrada");
        }

        if (routeRepository.findByRouteName(dto.routeName()).isPresent()
                && !route.getRouteName().equals(dto.routeName())) {
            return ApiResponseDTO.error(409, "Já existe uma rota com este nome.");
        }

        String oldName = route.getRouteName();
        route.setRouteName(dto.routeName());
        route.setRouteDescription(dto.routeDescription());
        route.setType(dto.type());

        Route savedRoute = routeRepository.save(route);
        RouteResponseDTO responseDTO = routeMapper.toResponseDTO(savedRoute);

        auditService.log(
            AuditAction.UPDATE,
            AuditEntity.ROUTE,
            routeId.toString(),
            "Rota atualizada: " + dto.routeName(),
            oldName,
            dto.routeName()
        );

        return ApiResponseDTO.success(200, "Rota atualizada com sucesso", responseDTO);
    }
}
