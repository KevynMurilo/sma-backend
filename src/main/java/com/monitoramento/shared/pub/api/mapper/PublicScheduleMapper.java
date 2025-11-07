package com.monitoramento.shared.pub.api.mapper;

import com.monitoramento.shared.pub.api.dto.PublicScheduleDTO;
import com.monitoramento.transit.domain.model.Schedule;
import com.monitoramento.transit.domain.model.ScheduleDeparture;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublicScheduleMapper {

    public PublicScheduleDTO toPublicDTO(Schedule schedule) {
        List<LocalTime> departureTimes = schedule.getDepartures() != null
                ? schedule.getDepartures().stream()
                    .map(ScheduleDeparture::getDepartureTime)
                    .sorted()
                    .collect(Collectors.toList())
                : List.of();

        return new PublicScheduleDTO(
                schedule.getId(),
                schedule.getDayProfile(),
                departureTimes
        );
    }
}
