package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.api.mapper.StopMapper;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStopByIdUseCase {

    private final StopRepository stopRepository;
    private final StopMapper stopMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<StopResponseDTO> execute(UUID stopId) {
        Stop stop = stopRepository.findById(stopId)
                .orElse(null);

        if (stop == null) {
            return ApiResponseDTO.empty(404, "Parada não encontrada");
        }

        StopResponseDTO responseDTO = stopMapper.toResponseDTO(stop);
        return ApiResponseDTO.success(200, "Parada encontrada", responseDTO);
    }
}
