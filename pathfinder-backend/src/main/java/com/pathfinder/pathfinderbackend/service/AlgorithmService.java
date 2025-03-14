package com.pathfinder.pathfinderbackend.service;


import com.pathfinder.pathfinderbackend.algorithm.AlgorithmFactory;
import com.pathfinder.pathfinderbackend.algorithm.PathfindingAlgorithm;
import com.pathfinder.pathfinderbackend.model.AlgorithmResult;
import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import com.pathfinder.pathfinderbackend.repository.AlgorithmResultRepository;
import com.pathfinder.pathfinderbackend.repository.MapRepository;
import com.pathfinder.pathfinderbackend.repository.NodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AlgorithmService {

    private final AlgorithmResultRepository algorithmResultRepository;
    private final MapRepository mapRepository;
    private final NodeRepository nodeRepository;
    private final AlgorithmFactory algorithmFactory;

    @Transactional
    public AlgorithmResult executeAlgorithm(Long mapId, Long startNodeId, Long endNodeId, AlgorithmType algorithmType) {
        Optional<Map> mapOpt = mapRepository.findById(mapId);
        Optional<Node> startNodeOpt = nodeRepository.findById(startNodeId);
        Optional<Node> endNodeOpt = nodeRepository.findById(endNodeId);

        if (mapOpt.isEmpty() || startNodeOpt.isEmpty() || endNodeOpt.isEmpty()) {
            throw new IllegalArgumentException("Map or nodes not found");
        }

        Map map = mapOpt.get();
        Node startNode = startNodeOpt.get();
        Node endNode = endNodeOpt.get();

        // Check if nodes belong to the specified map
        if (!startNode.getMap().getId().equals(mapId) || !endNode.getMap().getId().equals(mapId)) {
            throw new IllegalArgumentException("Nodes must belong to the specified map");
        }

        // Create and execute algorithm
        PathfindingAlgorithm algorithm = algorithmFactory.createAlgorithm(algorithmType, map, startNode, endNode);
        AlgorithmResult result = algorithm.execute();

        // Save result to database
        return algorithmResultRepository.save(result);
    }

    @Transactional(readOnly = true)
    public List<AlgorithmResult> getResultsByMap(Long mapId) {
        Optional<Map> mapOpt = mapRepository.findById(mapId);

        if (mapOpt.isEmpty()) {
            throw new IllegalArgumentException("Map not found");
        }

        return algorithmResultRepository.findByMapOrderByExecutionTimeDesc(mapOpt.get());
    }

    @Transactional(readOnly = true)
    public Optional<AlgorithmResult> getResultById(Long resultId) {
        return algorithmResultRepository.findById(resultId);
    }
}
