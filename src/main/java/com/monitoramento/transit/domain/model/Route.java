package com.monitoramento.transit.domain.model;

import com.monitoramento.transit.domain.model.enums.RouteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "transit_routes")
@Getter
@Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String routeName;
    private String routeDescription;

    @Enumerated(EnumType.STRING)
    private RouteType type;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stopOrder ASC")
    private List<RouteStopAssignment> stops;
}