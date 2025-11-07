package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.api.mapper.StopMapper;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListStopsUseCase {

    private final StopRepository stopRepository;
    private final StopMapper stopMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<StopResponseDTO>> execute(Pageable pageable) {
        Page<Stop> stops = stopRepository.findAll(pageable);
        Page<StopResponseDTO> dtoPage = stops.map(stopMapper::toResponseDTO);
        PagedResponseDTO<StopResponseDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Paradas encontradas", paged);
    }
}
