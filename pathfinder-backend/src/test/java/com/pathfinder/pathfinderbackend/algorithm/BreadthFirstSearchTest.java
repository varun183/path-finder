package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class BreadthFirstSearchTest {

    private Map map;
    private Node startNode;
    private Node endNode;

    @BeforeEach
    void setUp() {
        map = Map.builder()
                .id(1L)
                .name("Test Map")
                .width(3)
                .height(3)
                .build();

        Set<Node> nodes = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        // Create a 3x3 grid of nodes
        Node[][] nodeGrid = new Node[3][3];
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Node node = Node.builder()
                        .id(y * 3 + x + 1L)
                        .map(map)
                        .x(x)
                        .y(y)
                        .terrainType(TerrainType.PLAIN)
                        .outgoingEdges(new HashSet<>())
                        .incomingEdges(new HashSet<>())
                        .build();

                nodeGrid[y][x] = node;
                nodes.add(node);
            }
        }

        // Set obstacle in the middle
        nodeGrid[1][1].setTerrainType(TerrainType.OBSTACLE);

        // Create edges (4-way connectivity)
        int[] dx = {1, 0, -1, 0};
        int[] dy = {0, 1, 0, -1};

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if (nodeGrid[y][x].getTerrainType() == TerrainType.OBSTACLE) continue;

                for (int d = 0; d < 4; d++) {
                    int nx = x + dx[d];
                    int ny = y + dy[d];

                    if (nx >= 0 && nx < 3 && ny >= 0 && ny < 3 &&
                            nodeGrid[ny][nx].getTerrainType() != TerrainType.OBSTACLE) {
                        Edge edge = Edge.builder()
                                .id((y * 3 + x) * 4 + d + 1L)
                                .source(nodeGrid[y][x])
                                .target(nodeGrid[ny][nx])
                                .map(map)
                                .weight(1.0)
                                .build();

                        edges.add(edge);
                        nodeGrid[y][x].getOutgoingEdges().add(edge);
                        nodeGrid[ny][nx].getIncomingEdges().add(edge);
                    }
                }
            }
        }

        map.setNodes(nodes);
        map.setEdges(edges);

        // Set start and end nodes
        startNode = nodeGrid[0][0]; // Top left
        endNode = nodeGrid[2][2];   // Bottom right
    }

    @Test
    void shouldFindPathAroundObstacle() {
        // Arrange
        BreadthFirstSearch bfs = new BreadthFirstSearch(map, startNode, endNode);

        // Act
        AlgorithmResult result = bfs.execute();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.isPathFound()).isTrue();
        assertThat(result.getPathNodeIds()).isNotEmpty();

        // Path should have 5 nodes (start -> around obstacle -> end)
        assertThat(result.getPathNodeIds().size()).isEqualTo(5);

        // First node should be start and last node should be ended
        assertThat(result.getPathNodeIds().getFirst()).isEqualTo(startNode.getId());
        assertThat(result.getPathNodeIds().getLast()).isEqualTo(endNode.getId());
    }
}
