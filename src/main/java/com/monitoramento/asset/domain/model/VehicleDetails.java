package com.monitoramento.asset.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "asset_vehicle_details")
@Getter
@Setter
public class VehicleDetails {

    @Id
    @Column(name = "asset_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "asset_id")
    private MonitoredAsset asset;

    @Column(unique = true)
    private String licensePlate;

    private String model;
    private String make;
    private int year;
}