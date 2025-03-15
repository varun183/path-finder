package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;

import java.util.List;
import java.util.Stack;

public class DepthFirstSearch extends PathfindingAlgorithm {

    public DepthFirstSearch(Map map, Node startNode, Node endNode) {
        super(map, startNode, endNode);
        this.algorithmType = AlgorithmType.DFS;
    }

    @Override
    protected List<Node> findPathInternal() {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        startNode.setVisited(true);

        while (!stack.isEmpty()) {
            Node current = stack.pop();
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
                    stack.push(neighbor);
                }
            }
        }

        // No path found
        return null;
    }
}
