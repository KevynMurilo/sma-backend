package com.monitoramento.user.api.controller;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.RouteResponseDTO;
import com.monitoramento.user.domain.service.UserDetailsImpl;
import com.monitoramento.user.domain.useCase.AddFavoriteRouteUseCase;
import com.monitoramento.user.domain.useCase.ListMyFavoriteRoutesUseCase;
import com.monitoramento.user.domain.useCase.RemoveFavoriteRouteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteRouteController {

    private final AddFavoriteRouteUseCase addFavoriteRouteUseCase;
    private final ListMyFavoriteRoutesUseCase listMyFavoriteRoutesUseCase;
    private final RemoveFavoriteRouteUseCase removeFavoriteRouteUseCase;

    @PostMapping("/{routeId}")
    public ResponseEntity<ApiResponseDTO<RouteResponseDTO>> addFavorite(
            @PathVariable UUID routeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<RouteResponseDTO> response = addFavoriteRouteUseCase.execute(routeId, userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/my-favorites")
    public ResponseEntity<ApiResponseDTO<List<RouteResponseDTO>>> getMyFavorites(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<List<RouteResponseDTO>> response = listMyFavoriteRoutesUseCase.execute(userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/{routeId}")
    public ResponseEntity<ApiResponseDTO<Void>> removeFavorite(
            @PathVariable UUID routeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ApiResponseDTO<Void> response = removeFavoriteRouteUseCase.execute(routeId, userDetails);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}