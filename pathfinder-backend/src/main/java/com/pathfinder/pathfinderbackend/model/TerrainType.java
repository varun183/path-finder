package com.pathfinder.pathfinderbackend.model;


import lombok.Getter;

@Getter
public enum TerrainType {
    PLAIN(1.0),
    GRASS(1.2),
    FOREST(1.8),
    MOUNTAIN(3.0),
    WATER(5.0),
    OBSTACLE(Double.POSITIVE_INFINITY);

    private final double movementCost;

    TerrainType(double movementCost) {
        this.movementCost = movementCost;
    }
}
