package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgorithmComparisonTest {

    private Map map;
    private Node startNode;
    private Node endNode;
    private AlgorithmFactory algorithmFactory;

    @BeforeEach
    void setUp() {
        // Create a more complex 5x5 grid with varying terrain and obstacles
        map = Map.builder()
                .id(1L)
                .name("Comparison Test Map")
                .width(5)
                .height(5)
                .build();

        Set<Node> nodes = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        Node[][] nodeGrid = new Node[5][5];

        // Create nodes with different terrain types
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                TerrainType terrainType;

                // Add some obstacles and varied terrain
                if ((x == 2 && y == 2) || (x == 1 && y == 3) || (x == 3 && y == 1)) {
                    terrainType = TerrainType.OBSTACLE;
                } else if (x == 2) {
                    terrainType = TerrainType.FOREST;
                } else if (y == 3) {
                    terrainType = TerrainType.MOUNTAIN;
                } else if (x == 0 || y == 0) {
                    terrainType = TerrainType.GRASS;
                } else {
                    terrainType = TerrainType.PLAIN;
                }

                Node node = Node.builder()
                        .id(y * 5 + x + 1L)
                        .map(map)
                        .x(x)
                        .y(y)
                        .terrainType(terrainType)
                        .outgoingEdges(new HashSet<>())
                        .incomingEdges(new HashSet<>())
                        .build();

                nodeGrid[y][x] = node;
                nodes.add(node);
            }
        }

        // Create edges (8-way connectivity)
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (nodeGrid[y][x].getTerrainType() == TerrainType.OBSTACLE) continue;

                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        if (dx == 0 && dy == 0) continue; // Skip self

                        int nx = x + dx;
                        int ny = y + dy;

                        if (nx >= 0 && nx < 5 && ny >= 0 && ny < 5 &&
                                nodeGrid[ny][nx].getTerrainType() != TerrainType.OBSTACLE) {

                            // Calculate weight based on terrain
                            double weight = (nodeGrid[y][x].getTerrainType().getMovementCost() +
                                    nodeGrid[ny][nx].getTerrainType().getMovementCost()) / 2;

                            // Apply diagonal penalty
                            if (dx != 0 && dy != 0) {
                                weight *= Math.sqrt(2); // Diagonal distance is √2 times longer
                            }

                            Edge edge = Edge.builder()
                                    .id((y * 5 + x) * 8 + (dy + 1) * 3 + (dx + 1) + 1L)
                                    .source(nodeGrid[y][x])
                                    .target(nodeGrid[ny][nx])
                                    .map(map)
                                    .weight(weight)
                                    .build();

                            edges.add(edge);
                            nodeGrid[y][x].getOutgoingEdges().add(edge);
                            nodeGrid[ny][nx].getIncomingEdges().add(edge);
                        }
                    }
                }
            }
        }

        map.setNodes(nodes);
        map.setEdges(edges);

        // Set start and end nodes
        startNode = nodeGrid[0][0]; // Top left
        endNode = nodeGrid[4][4];   // Bottom right

        algorithmFactory = new AlgorithmFactory();
    }

    @Test
    void shouldCompareAllAlgorithms() {
        // Test BFS
        PathfindingAlgorithm bfs = algorithmFactory.createAlgorithm(AlgorithmType.BFS, map, startNode, endNode);
        AlgorithmResult bfsResult = bfs.execute();

        // Test DFS
        PathfindingAlgorithm dfs = algorithmFactory.createAlgorithm(AlgorithmType.DFS, map, startNode, endNode);
        AlgorithmResult dfsResult = dfs.execute();

        // Test Dijkstra
        PathfindingAlgorithm dijkstra = algorithmFactory.createAlgorithm(AlgorithmType.DIJKSTRA, map, startNode, endNode);
        AlgorithmResult dijkstraResult = dijkstra.execute();

        // Test A*
        PathfindingAlgorithm aStar = algorithmFactory.createAlgorithm(AlgorithmType.A_STAR, map, startNode, endNode);
        AlgorithmResult aStarResult = aStar.execute();

        // All algorithms should find a path
        assertThat(bfsResult.isPathFound()).isTrue();
        assertThat(dfsResult.isPathFound()).isTrue();
        assertThat(dijkstraResult.isPathFound()).isTrue();
        assertThat(aStarResult.isPathFound()).isTrue();

        // BFS typically explores more nodes than A* on weighted graphs
        assertThat(bfsResult.getNodesExplored()).isGreaterThanOrEqualTo(aStarResult.getNodesExplored());

        // Dijkstra and A* should find optimal paths (same path length)
        assertThat(dijkstraResult.getPathLength()).isEqualTo(aStarResult.getPathLength());

        // DFS often finds a longer path
        assertThat(dfsResult.getPathLength()).isGreaterThanOrEqualTo(aStarResult.getPathLength());

        System.out.println("BFS: " + bfsResult.getNodesExplored() + " nodes explored, path length: " + bfsResult.getPathLength());
        System.out.println("DFS: " + dfsResult.getNodesExplored() + " nodes explored, path length: " + dfsResult.getPathLength());
        System.out.println("Dijkstra: " + dijkstraResult.getNodesExplored() + " nodes explored, path length: " + dijkstraResult.getPathLength());
        System.out.println("A*: " + aStarResult.getNodesExplored() + " nodes explored, path length: " + aStarResult.getPathLength());
    }
}
