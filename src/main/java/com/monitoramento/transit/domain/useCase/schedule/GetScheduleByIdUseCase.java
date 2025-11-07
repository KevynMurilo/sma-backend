package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetScheduleByIdUseCase {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<ScheduleResponseDTO> execute(UUID scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElse(null);

        if (schedule == null) {
            return ApiResponseDTO.empty(404, "Horário não encontrado");
        }

        ScheduleResponseDTO responseDTO = scheduleMapper.toResponseDTO(schedule);
        return ApiResponseDTO.success(200, "Horário encontrado", responseDTO);
    }
}
