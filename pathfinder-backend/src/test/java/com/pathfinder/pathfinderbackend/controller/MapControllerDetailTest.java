package com.pathfinder.pathfinderbackend.controller;

import com.pathfinder.pathfinderbackend.model.Edge;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import com.pathfinder.pathfinderbackend.model.TerrainType;
import com.pathfinder.pathfinderbackend.service.MapService;
import com.pathfinder.pathfinderbackend.service.MapGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MapControllerDetailTest {

    private MockMvc mockMvc;

    @Mock
    private MapService mapService;

    @Mock
    private MapGeneratorService mapGeneratorService;

    @BeforeEach
    public void setup() {
        MapController mapController = new MapController(mapService, mapGeneratorService);
        mockMvc = MockMvcBuilders.standaloneSetup(mapController).build();
    }

    @Test
    public void shouldReturnMapWithNodesAndEdges() throws Exception {
        // Arrange
        Map map = Map.builder()
                .id(1L)
                .name("Test Map with Nodes")
                .width(3)
                .height(3)
                .build();

        Set<Node> nodes = new HashSet<>();
        Set<Edge> edges = new HashSet<>();

        // Create nodes
        Node node1 = Node.builder()
                .id(1L)
                .map(map)
                .x(0)
                .y(0)
                .terrainType(TerrainType.PLAIN)
                .build();

        Node node2 = Node.builder()
                .id(2L)
                .map(map)
                .x(0)
                .y(1)
                .terrainType(TerrainType.GRASS)
                .build();

        nodes.add(node1);
        nodes.add(node2);

        // Create edge
        Edge edge = Edge.builder()
                .id(1L)
                .source(node1)
                .target(node2)
                .map(map)
                .weight(1.0)
                .build();

        edges.add(edge);

        // Set relationships
        map.setNodes(nodes);
        map.setEdges(edges);

        when(mapService.getMapById(1L)).thenReturn(Optional.of(map));

        // Act & Assert
        mockMvc.perform(get("/api/maps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.map.id").value(1))
                .andExpect(jsonPath("$.map.name").value("Test Map with Nodes"))
                .andExpect(jsonPath("$.nodes.length()").value(2))
                .andExpect(jsonPath("$.nodes[0].id").exists())
                .andExpect(jsonPath("$.nodes[0].terrainType").exists())
                .andExpect(jsonPath("$.edges.length()").value(1))
                .andExpect(jsonPath("$.edges[0].id").value(1))
                .andExpect(jsonPath("$.edges[0].sourceId").exists())
                .andExpect(jsonPath("$.edges[0].targetId").exists());
    }
}