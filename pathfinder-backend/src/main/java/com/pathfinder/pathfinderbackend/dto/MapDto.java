package com.pathfinder.pathfinderbackend.dto;


import com.pathfinder.pathfinderbackend.model.Map;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MapDto {
    private Long id;
    private String name;
    private String description;
    private int width;
    private int height;
    private int nodeCount;
    private int edgeCount;
    private LocalDateTime createdAt;

    public static MapDto fromEntity(Map map) {
        return MapDto.builder()
                .id(map.getId())
                .name(map.getName())
                .description(map.getDescription())
                .width(map.getWidth())
                .height(map.getHeight())
                .nodeCount(map.getNodes().size())
                .edgeCount(map.getEdges().size())
                .createdAt(map.getCreatedAt())
                .build();
    }
}
