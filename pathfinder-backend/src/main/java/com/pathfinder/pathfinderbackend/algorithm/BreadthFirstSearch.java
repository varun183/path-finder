package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearch extends PathfindingAlgorithm {

    public BreadthFirstSearch(Map map, Node startNode, Node endNode) {
        super(map, startNode, endNode);
        this.algorithmType = AlgorithmType.BFS;
    }

    @Override
    protected List<Node> findPathInternal() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(startNode);
        startNode.setVisited(true);

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            nodesExplored++;

            // If we reached the target node, reconstruct and return the path
            if (current.equals(endNode)) {
                return reconstructPath(current);
            }

            // Explore neighbors
            for (Edge edge : current.getOutgoingEdges()) {
                Node neighbor = edge.getTarget();

                if (!neighbor.isVisited() && Double.isFinite(edge.getWeight())) {
                    neighbor.setVisited(true);
                    neighbor.setParent(current);
                    queue.add(neighbor);
                }
            }
        }

        // No path found
        return null;
    }
}
