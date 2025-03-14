package com.pathfinder.pathfinderbackend.service;


import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import com.pathfinder.pathfinderbackend.model.TerrainType;
import com.pathfinder.pathfinderbackend.repository.EdgeRepository;
import com.pathfinder.pathfinderbackend.repository.MapRepository;
import com.pathfinder.pathfinderbackend.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MapGeneratorService {

    private final MapRepository mapRepository;
    private final NodeRepository nodeRepository;
    private final EdgeRepository edgeRepository;
    private final Random random = new Random();

    @Transactional
    public Map generateRandomMap(String name, String description, int width, int height, double obstacleDensity) {
        // Create base map
        Map map = Map.builder()
                .name(name)
                .description(description)
                .width(width)
                .height(height)
                .build();

        map = mapRepository.save(map);

        // Create nodes with random terrain
        createNodes(map, obstacleDensity);

        // Create edges between nodes
        createEdges(map);

        return mapRepository.findById(map.getId()).orElseThrow();
    }

    private void createNodes(Map map, double obstacleDensity) {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                TerrainType terrainType = getRandomTerrainType(obstacleDensity);

                Node node = Node.builder()
                        .map(map)
                        .x(x)
                        .y(y)
                        .terrainType(terrainType)
                        .build();

                nodeRepository.save(node);
            }
        }
    }

    private void createEdges(Map map) {
        // Load all nodes for this map
        var nodes = nodeRepository.findByMap(map);

        // Create edges between adjacent nodes
        for (Node node : nodes) {
            if (node.getTerrainType() == TerrainType.OBSTACLE) {
                continue; // Skip obstacles
            }

            // Check all 8 adjacent cells
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue; // Skip self

                    int nx = node.getX() + dx;
                    int ny = node.getY() + dy;

                    // Skip if out of bounds
                    if (nx < 0 || nx >= map.getWidth() || ny < 0 || ny >= map.getHeight()) {
                        continue;
                    }

                    Optional<Node> neighborOpt = nodeRepository.findByMapAndCoordinates(map.getId(), nx, ny);

                    if (neighborOpt.isPresent()) {
                        Node neighbor = neighborOpt.get();

                        if (neighbor.getTerrainType() != TerrainType.OBSTACLE) {
                            // Calculate weight based on terrain costs and diagonal penalty
                            double weight = (node.getTerrainType().getMovementCost() +
                                    neighbor.getTerrainType().getMovementCost()) / 2;

                            // Apply diagonal penalty
                            if (dx != 0 && dy != 0) {
                                weight *= Math.sqrt(2); // Diagonal distance is √2 times longer
                            }

                            Edge edge = Edge.builder()
                                    .source(node)
                                    .target(neighbor)
                                    .weight(weight)
                                    .map(map)
                                    .build();

                            edgeRepository.save(edge);
                        }
                    }
                }
            }
        }
    }

    private TerrainType getRandomTerrainType(double obstacleDensity) {
        if (random.nextDouble() < obstacleDensity) {
            return TerrainType.OBSTACLE;
        }

        double rand = random.nextDouble();

        if (rand < 0.5) {
            return TerrainType.PLAIN;
        } else if (rand < 0.7) {
            return TerrainType.GRASS;
        } else if (rand < 0.85) {
            return TerrainType.FOREST;
        } else if (rand < 0.95) {
            return TerrainType.MOUNTAIN;
        } else {
            return TerrainType.WATER;
        }
    }
}
