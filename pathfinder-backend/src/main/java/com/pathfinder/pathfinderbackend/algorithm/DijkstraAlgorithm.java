package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraAlgorithm extends PathfindingAlgorithm {

    public DijkstraAlgorithm(Map map, Node startNode, Node endNode) {
        super(map, startNode, endNode);
        this.algorithmType = AlgorithmType.DIJKSTRA;
    }

    @Override
    protected List<Node> findPathInternal() {
        // Priority queue ordered by distance from start
        PriorityQueue<Node> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(Node::getDistanceFromStart));

        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            nodesExplored++;

            // Skip if node was already visited
            if (current.isVisited()) {
                continue;
            }

            current.setVisited(true);

            // If we reached the target node, reconstruct and return the path
            if (current.equals(endNode)) {
                return reconstructPath(current);
            }

            // Explore neighbors
            for (Edge edge : current.getOutgoingEdges()) {
                Node neighbor = edge.getTarget();

                if (neighbor.isVisited() || !Double.isFinite(edge.getWeight())) {
                    continue;
                }

                double newDistance = current.getDistanceFromStart() + edge.getWeight();

                if (newDistance < neighbor.getDistanceFromStart()) {
                    neighbor.setDistanceFromStart(newDistance);
                    neighbor.setParent(current);
                    openSet.add(neighbor);
                }
            }
        }

        // No path found
        return null;
    }
}
