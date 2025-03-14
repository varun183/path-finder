package com.pathfinder.pathfinderbackend.dto;


import com.pathfinder.pathfinderbackend.model.Node;
import com.pathfinder.pathfinderbackend.model.TerrainType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeDto {
    private Long id;
    private int x;
    private int y;
    private TerrainType terrainType;

    public static NodeDto fromEntity(Node node) {
        return NodeDto.builder()
                .id(node.getId())
                .x(node.getX())
                .y(node.getY())
                .terrainType(node.getTerrainType())
                .build();
    }
}
