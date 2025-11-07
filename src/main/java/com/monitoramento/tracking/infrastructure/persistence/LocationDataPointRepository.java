package com.monitoramento.tracking.infrastructure.persistence;

import com.monitoramento.tracking.domain.model.LocationDataPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface LocationDataPointRepository extends JpaRepository<LocationDataPoint, Long> {

    Page<LocationDataPoint> findByDeviceId(UUID deviceId, Pageable pageable);

    Page<LocationDataPoint> findByDeviceIdAndTimestampBetween(UUID deviceId, OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    Page<LocationDataPoint> findByDeviceIdAndTimestampAfter(UUID deviceId, OffsetDateTime start, Pageable pageable);

    Page<LocationDataPoint> findByDeviceIdAndTimestampBefore(UUID deviceId, OffsetDateTime end, Pageable pageable);

    List<LocationDataPoint> findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(UUID deviceId, OffsetDateTime start, OffsetDateTime end);
}