package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListSchedulesUseCase {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<ScheduleResponseDTO>> execute(Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);
        Page<ScheduleResponseDTO> dtoPage = schedules.map(scheduleMapper::toResponseDTO);
        PagedResponseDTO<ScheduleResponseDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Horários encontrados", paged);
    }
}
