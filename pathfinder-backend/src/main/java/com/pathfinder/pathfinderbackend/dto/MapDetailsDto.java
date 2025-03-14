package com.pathfinder.pathfinderbackend.dto;

import com.pathfinder.pathfinderbackend.model.Map;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class MapDetailsDto {
    private MapDto map;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;

    public static MapDetailsDto fromEntity(Map map) {
        List<NodeDto> nodeDtos = map.getNodes().stream()
                .map(NodeDto::fromEntity)
                .collect(Collectors.toList());

        List<EdgeDto> edgeDtos = map.getEdges().stream()
                .map(EdgeDto::fromEntity)
                .collect(Collectors.toList());

        return MapDetailsDto.builder()
                .map(MapDto.fromEntity(map))
                .nodes(nodeDtos)
                .edges(edgeDtos)
                .build();
    }
}
