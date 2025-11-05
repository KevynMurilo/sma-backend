package com.monitoramento.transit.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import java.util.UUID;

@Entity
@Table(name = "transit_stops")
@Getter
@Setter
public class Stop {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(columnDefinition = "geometry(Point,4326)", nullable = false)
    private Point location;
}