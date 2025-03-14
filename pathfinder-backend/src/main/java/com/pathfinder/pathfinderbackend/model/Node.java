package com.pathfinder.pathfinderbackend.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "node")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    @Column(name = "terrain_type")
    private TerrainType terrainType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private Map map;

    @OneToMany(mappedBy = "source")
    @Builder.Default
    private Set<Edge> outgoingEdges = new HashSet<>();

    @OneToMany(mappedBy = "target")
    @Builder.Default
    private Set<Edge> incomingEdges = new HashSet<>();

    // Transient properties for pathfinding algorithms
    @Transient
    @Builder.Default
    private double distanceFromStart = Double.POSITIVE_INFINITY;

    @Transient
    @Builder.Default
    private double estimatedDistanceToGoal = Double.POSITIVE_INFINITY;

    @Transient
    @Builder.Default
    private Node parent=null;

    @Transient
    private boolean visited;

    public double getTotalCost() {
        return distanceFromStart + estimatedDistanceToGoal;
    }

    public void reset() {
        distanceFromStart = Double.POSITIVE_INFINITY;
        estimatedDistanceToGoal = Double.POSITIVE_INFINITY;
        parent = null;
        visited = false;
    }
}
