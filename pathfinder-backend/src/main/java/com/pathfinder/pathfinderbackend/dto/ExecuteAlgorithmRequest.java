package com.pathfinder.pathfinderbackend.dto;


import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ExecuteAlgorithmRequest {
    @NotNull(message = "Start node ID is required")
    private Long startNodeId;

    @NotNull(message = "End node ID is required")
    private Long endNodeId;

    @NotNull(message = "Algorithm type is required")
    private AlgorithmType algorithmType;
}
