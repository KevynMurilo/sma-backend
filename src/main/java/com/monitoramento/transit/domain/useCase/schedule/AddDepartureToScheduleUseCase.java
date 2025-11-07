package com.monitoramento.transit.domain.useCase.schedule;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.ScheduleDepartureDTO;
import com.monitoramento.transit.api.dto.ScheduleResponseDTO;
import com.monitoramento.transit.api.mapper.ScheduleDepartureMapper;
import com.monitoramento.transit.api.mapper.ScheduleMapper;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.domain.model.ScheduleDeparture;
import com.monitoramento.transit.infrastructure.persistence.ScheduleDepartureRepository;
import com.monitoramento.transit.infrastructure.persistence.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddDepartureToScheduleUseCase {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleDepartureRepository scheduleDepartureRepository;
    private final ScheduleMapper scheduleMapper;
    private final ScheduleDepartureMapper scheduleDepartureMapper;

    @Transactional
    public ApiResponseDTO<ScheduleResponseDTO> execute(UUID scheduleId, ScheduleDepartureDTO dto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElse(null);
        if (schedule == null) {
            return ApiResponseDTO.empty(404, "Agendamento não encontrado");
        }

        if (scheduleDepartureRepository.findByScheduleIdAndDepartureTime(scheduleId, dto.departureTime()).isPresent()) {
            return ApiResponseDTO.error(409, "Este horário de partida já existe neste agendamento.");
        }

        ScheduleDeparture departure = scheduleDepartureMapper.toEntity(dto, schedule);
        scheduleDepartureRepository.save(departure);

        Schedule updatedSchedule = scheduleRepository.findById(scheduleId).get();
        return ApiResponseDTO.success(201, "Partida adicionada", scheduleMapper.toResponseDTO(updatedSchedule));
    }
}