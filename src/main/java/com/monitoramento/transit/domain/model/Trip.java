package com.monitoramento.transit.domain.model;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.transit.domain.model.enums.TripStatus;
import com.monitoramento.user.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "transit_trips", indexes = {
        @Index(name = "idx_trip_asset_date", columnList = "asset_id, tripDate"),
        @Index(name = "idx_trip_driver_date", columnList = "driver_id, tripDate")
})
@Getter
@Setter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_departure_id", nullable = false)
    private ScheduleDeparture scheduleDeparture;

    @Column(nullable = false)
    private LocalDate tripDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private MonitoredAsset asset;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.SCHEDULED;

    private OffsetDateTime actualStartTime;
    private OffsetDateTime actualEndTime;
}