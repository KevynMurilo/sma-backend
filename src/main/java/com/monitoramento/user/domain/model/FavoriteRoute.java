package com.monitoramento.user.domain.model;

import com.monitoramento.transit.domain.model.Route;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "user_favorite_routes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "route_id"})
})
@Getter
@Setter
public class FavoriteRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;
}