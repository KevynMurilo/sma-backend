package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.infrastructure.persistence.ScheduleDepartureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RemoveDepartureFromScheduleUseCase {

    private final ScheduleDepartureRepository scheduleDepartureRepository;

    @Transactional
    public ApiResponseDTO<Void> execute(Long departureId) {
        if (!scheduleDepartureRepository.existsById(departureId)) {
            return ApiResponseDTO.empty(404, "Partida não encontrada");
        }

        scheduleDepartureRepository.deleteById(departureId);
        return ApiResponseDTO.success(200, "Partida removida com sucesso");
    }
}