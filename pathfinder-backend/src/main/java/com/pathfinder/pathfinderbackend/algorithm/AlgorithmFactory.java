package com.pathfinder.pathfinderbackend.algorithm;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import org.springframework.stereotype.Component;

@Component
public class AlgorithmFactory {

    public PathfindingAlgorithm createAlgorithm(AlgorithmType type, Map map, Node startNode, Node endNode) {
        return switch (type) {
            case BFS -> new BreadthFirstSearch(map, startNode, endNode);
            case DFS -> new DepthFirstSearch(map, startNode, endNode);
            case DIJKSTRA -> new DijkstraAlgorithm(map, startNode, endNode);
            case A_STAR -> new AStarAlgorithm(map, startNode, endNode);
        };
    }
}
