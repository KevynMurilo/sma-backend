package com.monitoramento.transit.domain.useCase.trip;

import com.monitoramento.shared.dto.ApiResponseDTO;
import com.monitoramento.shared.dto.PagedResponseDTO;
import com.monitoramento.transit.api.dto.TripResponseDTO;
import com.monitoramento.transit.api.mapper.TripMapper;
import com.monitoramento.transit.domain.model.Trip;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.transit.infrastructure.persistence.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListTripsUseCase {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    @Transactional(readOnly = true)
    public ApiResponseDTO<PagedResponseDTO<TripResponseDTO>> execute(
            Pageable pageable,
            LocalDate date,
            TripStatus status,
            UUID assetId,
            UUID driverId) {

        Specification<Trip> spec = Specification.where(null);

        if (date != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("tripDate"), date));
        }

        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        if (assetId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("asset").get("id"), assetId));
        }

        if (driverId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("driver").get("id"), driverId));
        }

        Page<Trip> trips = tripRepository.findAll(spec, pageable);
        Page<TripResponseDTO> dtoPage = trips.map(tripMapper::toResponseDTO);
        PagedResponseDTO<TripResponseDTO> paged = PagedResponseDTO.from(dtoPage);

        return ApiResponseDTO.success(200, "Viagens encontradas", paged);
    }
}
