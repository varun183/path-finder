package com.pathfinder.pathfinderbackend.dto;


import com.pathfinder.pathfinderbackend.model.Edge;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EdgeDto {
    private Long id;
    private Long sourceId;
    private Long targetId;
    private double weight;

    public static EdgeDto fromEntity(Edge edge) {
        return EdgeDto.builder()
                .id(edge.getId())
                .sourceId(edge.getSource().getId())
                .targetId(edge.getTarget().getId())
                .weight(edge.getWeight())
                .build();
    }
}
