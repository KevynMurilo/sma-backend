package com.monitoramento.asset.domain.useCase.device;

import com.monitoramento.asset.api.dto.TrackingDeviceResponseDTO;
import com.monitoramento.asset.api.mapper.TrackingDeviceMapper;
import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListUnassignedDevicesUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final TrackingDeviceMapper trackingDeviceMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<TrackingDeviceResponseDTO>> execute(Pageable pageable) {
        Page<TrackingDeviceResponseDTO> dtoPage = trackingDeviceRepository.findByStatus(DeviceStatus.UNASSIGNED, pageable)
                .map(trackingDeviceMapper::trackingDeviceToTrackingDeviceResponseDTO);

        PagedResponseDTO<TrackingDeviceResponseDTO> pagedResponse = PagedResponseDTO.from(dtoPage);
        return ApiResponseDTO.success(200, "Dispositivos não alocados encontrados", pagedResponse);
    }
}