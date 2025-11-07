package com.monitoramento.transit.domain.useCase.stop;

import com.monitoramento.shared.audit.domain.model.enums.AuditAction;
import com.monitoramento.shared.audit.domain.model.enums.AuditEntity;
import com.monitoramento.shared.audit.domain.service.AuditService;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.transit.api.dto.StopResponseDTO;
import com.monitoramento.transit.api.dto.StopUpdateDTO;
import com.monitoramento.transit.api.mapper.StopMapper;
import com.monitoramento.transit.domain.model.Stop;
import com.monitoramento.transit.infrastructure.persistence.StopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateStopUseCase {

    private final StopRepository stopRepository;
    private final StopMapper stopMapper;
    private final AuditService auditService;

    @Transactional
    public ApiResponseDTO<StopResponseDTO> execute(UUID stopId, StopUpdateDTO dto) {
        Stop stop = stopRepository.findById(stopId)
                .orElse(null);

        if (stop == null) {
            return ApiResponseDTO.empty(404, "Parada não encontrada");
        }

        if (stopRepository.findByName(dto.name()).isPresent()
                && !stop.getName().equals(dto.name())) {
            return ApiResponseDTO.error(409, "Já existe uma parada com este nome.");
        }

        String oldName = stop.getName();
        stop.setName(dto.name());
        stop.setDescription(dto.description());
        stop.setLatitude(dto.latitude());
        stop.setLongitude(dto.longitude());

        Stop savedStop = stopRepository.save(stop);
        StopResponseDTO responseDTO = stopMapper.toResponseDTO(savedStop);

        auditService.log(
            AuditAction.UPDATE,
            AuditEntity.STOP,
            stopId.toString(),
            "Parada atualizada: " + dto.name(),
            oldName,
            dto.name()
        );

        return ApiResponseDTO.success(200, "Parada atualizada com sucesso", responseDTO);
    }
}
