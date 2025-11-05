package com.monitoramento.tracking.domain.model;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import com.monitoramento.tracking.domain.model.enums.AssetStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "asset_current_status")
@Getter
@Setter
public class AssetCurrentStatus {

    @Id
    @Column(name = "asset_id")
    private UUID id; // Mesma chave do MonitoredAsset

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "asset_id")
    private MonitoredAsset asset;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_location_id")
    private LocationDataPoint lastLocation;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point lastCoordinates;

    private OffsetDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetStatus status = AssetStatus.OFFLINE;
}