package com.monitoramento.tracking.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "location_history", indexes = {
        @Index(name = "idx_location_device_time", columnList = "deviceId, timestamp DESC")
})
@Getter
@Setter
public class LocationDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "deviceId")
    private UUID deviceId;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point coordinates;

    @Column(nullable = false)
    private OffsetDateTime timestamp;

    private Double speed;
    private Double heading;
    private Double altitude;
}