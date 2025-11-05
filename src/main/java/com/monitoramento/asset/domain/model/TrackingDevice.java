package com.monitoramento.asset.domain.model;

import com.monitoramento.asset.domain.model.enums.DeviceStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "tracking_devices")
@Getter
@Setter
public class TrackingDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String deviceSerial;

    private String model;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceStatus status = DeviceStatus.UNASSIGNED;

    @OneToOne(mappedBy = "trackingDevice")
    private MonitoredAsset assignedAsset;
}