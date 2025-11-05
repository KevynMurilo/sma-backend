package com.monitoramento.asset.domain.model;

import com.monitoramento.asset.domain.model.enums.AssetType;
import com.monitoramento.organization.domain.model.Fleet;
import com.monitoramento.user.domain.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "monitored_assets")
@Getter
@Setter
public class MonitoredAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetType type;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", unique = true)
    private TrackingDevice trackingDevice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_driver_id")
    private User currentDriver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id")
    private Fleet fleet;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isPubliclyVisible = false;
}