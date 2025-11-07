package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.StopDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.api.mapper.StopMapper;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateStopUseCase {

    private final StopRepository stopRepository;
    private final StopMapper stopMapper;

    @Transactional
    public ApiResponseDTO<StopResponseDTO> execute(StopDTO dto) {
        if (stopRepository.findByName(dto.name()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe uma parada com este nome.");
        }

        Stop stop = stopMapper.toEntity(dto);
        Stop savedStop = stopRepository.save(stop);
        return ApiResponseDTO.success(201, "Parada criada com sucesso", stopMapper.toResponseDTO(savedStop));
    }
}