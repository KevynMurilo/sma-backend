package com.monitoramento.organization.domain.model;

import com.monitoramento.asset.domain.model.MonitoredAsset;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "fleets")
@Getter
@Setter
public class Fleet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @OneToMany(mappedBy = "fleet")
    private Set<MonitoredAsset> assets;
}