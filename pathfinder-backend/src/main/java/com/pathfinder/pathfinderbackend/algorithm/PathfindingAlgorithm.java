package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public abstract class PathfindingAlgorithm {

    protected final Map map;
    protected final Node startNode;
    protected final Node endNode;

    @Getter
    protected AlgorithmType algorithmType;

    @Getter
    protected int nodesExplored = 0;

    protected long startTime;
    protected long endTime;

    protected abstract List<Node> findPathInternal();

    public AlgorithmResult execute() {
        // Reset all nodes before starting
        map.getNodes().forEach(Node::reset);

        // Set start node's distance to 0
        startNode.setDistanceFromStart(0);

        // Record start time and memory
        startTime = System.currentTimeMillis();
        long memoryBefore = getUsedMemory();

        // Execute algorithm
        List<Node> path = findPathInternal();

        // Record end time and memory
        endTime = System.currentTimeMillis();
        long memoryAfter = getUsedMemory();

        // Calculate metrics
        long executionTime = endTime - startTime;
        long memoryUsed = memoryAfter - memoryBefore;

        // Convert path to ID list for persistence
        List<Long> pathNodeIds = new ArrayList<>();
        if (path != null) {
            for (Node node : path) {
                pathNodeIds.add(node.getId());
            }
        }

        // Create and return result
        return AlgorithmResult.builder()
                .map(map)
                .algorithmType(algorithmType)
                .executionDurationMs(executionTime)
                .memoryUsageBytes(memoryUsed)
                .nodesExplored(nodesExplored)
                .pathLength(calculatePathLength(path))
                .pathFound(path != null && !path.isEmpty())
                .startNode(startNode)
                .endNode(endNode)
                .pathNodeIds(pathNodeIds)
                .build();
    }

    protected double calculatePathLength(List<Node> path) {
        if (path == null || path.size() < 2) {
            return 0;
        }

        double length = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);

            // Find edge weight between these nodes
            double weight = current.getOutgoingEdges().stream()
                    .filter(e -> e.getTarget().equals(next))
                    .findFirst()
                    .map(Edge::getWeight)
                    .orElse(0.0);

            length += weight;
        }

        return length;
    }

    protected List<Node> reconstructPath(Node current) {
        List<Node> path = new ArrayList<>();

        while (current != null) {
            path.add(current);
            current = current.getParent();
        }

        Collections.reverse(path);
        return path;
    }

    private long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
