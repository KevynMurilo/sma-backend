package com.monitoramento.asset.domain.useCase.device;

import com.monitoramento.asset.api.dto.TrackingDeviceCreateDTO;
import com.monitoramento.asset.api.dto.TrackingDeviceResponseDTO;
import com.monitoramento.asset.api.mapper.TrackingDeviceMapper;
import com.monitoramento.asset.domain.model.TrackingDevice;
import com.monitoramento.asset.infrastructure.persistence.TrackingDeviceRepository;
import com.monitoramento.shared.dto.ApiResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTrackingDeviceUseCase {

    private final TrackingDeviceRepository trackingDeviceRepository;
    private final TrackingDeviceMapper trackingDeviceMapper;

    @Transactional
    public ApiResponseDTO<TrackingDeviceResponseDTO> execute(TrackingDeviceCreateDTO dto) {
        if (trackingDeviceRepository.findByDeviceSerial(dto.deviceSerial()).isPresent()) {
            return ApiResponseDTO.error(409, "Já existe um dispositivo com este serial.");
        }

        TrackingDevice device = trackingDeviceMapper.trackingDeviceCreateDTOToTrackingDevice(dto);
        TrackingDevice savedDevice = trackingDeviceRepository.save(device);
        TrackingDeviceResponseDTO responseDTO = trackingDeviceMapper.trackingDeviceToTrackingDeviceResponseDTO(savedDevice);

        return ApiResponseDTO.success(201, "Dispositivo criado com sucesso", responseDTO);
    }
}