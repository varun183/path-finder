package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm extends PathfindingAlgorithm {

    public AStarAlgorithm(Map map, Node startNode, Node endNode) {
        super(map, startNode, endNode);
        this.algorithmType = AlgorithmType.A_STAR;
    }

    @Override
    protected List<Node> findPathInternal() {
        // Priority queue ordered by total cost (f = g + h)
        PriorityQueue<Node> openSet = new PriorityQueue<>(
                Comparator.comparingDouble(Node::getTotalCost));

        // Set initial heuristic for start node
        startNode.setEstimatedDistanceToGoal(calculateHeuristic(startNode, endNode));
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
                    neighbor.setEstimatedDistanceToGoal(calculateHeuristic(neighbor, endNode));
                    neighbor.setParent(current);
                    openSet.add(neighbor);
                }
            }
        }

        // No path found
        return null;
    }

    private double calculateHeuristic(Node from, Node to) {
        // Using Euclidean distance as the heuristic
        int dx = from.getX() - to.getX();
        int dy = from.getY() - to.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }
}
