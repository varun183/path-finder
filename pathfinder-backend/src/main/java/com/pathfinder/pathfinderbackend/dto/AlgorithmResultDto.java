package com.pathfinder.pathfinderbackend.dto;


import com.pathfinder.pathfinderbackend.model.AlgorithmResult;
import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AlgorithmResultDto {
    private Long id;
    private Long mapId;
    private AlgorithmType algorithmType;
    private LocalDateTime executionTime;
    private long executionDurationMs;
    private long memoryUsageBytes;
    private int nodesExplored;
    private double pathLength;
    private boolean pathFound;
    private Long startNodeId;
    private Long endNodeId;
    private List<Long> pathNodeIds;

    public static AlgorithmResultDto fromEntity(AlgorithmResult result) {
        return AlgorithmResultDto.builder()
                .id(result.getId())
                .mapId(result.getMap().getId())
                .algorithmType(result.getAlgorithmType())
                .executionTime(result.getExecutionTime())
                .executionDurationMs(result.getExecutionDurationMs())
                .memoryUsageBytes(result.getMemoryUsageBytes())
                .nodesExplored(result.getNodesExplored())
                .pathLength(result.getPathLength())
                .pathFound(result.isPathFound())
                .startNodeId(result.getStartNode().getId())
                .endNodeId(result.getEndNode().getId())
                .pathNodeIds(result.getPathNodeIds())
                .build();
    }
}
