package com.pathfinder.pathfinderbackend.repository;



import com.pathfinder.pathfinderbackend.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EntityRelationshipsTest {

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Test
    public void shouldCreateMapWithNodesAndEdges() {
        // Create a map
        Map map = Map.builder()
                .name("Relationship Test Map")
                .description("Testing entity relationships")
                .width(5)
                .height(5)
                .build();

        map = mapRepository.save(map);

        // Create nodes
        Node node1 = Node.builder()
                .map(map)
                .x(0)
                .y(0)
                .terrainType(TerrainType.PLAIN)
                .build();

        Node node2 = Node.builder()
                .map(map)
                .x(1)
                .y(0)
                .terrainType(TerrainType.GRASS)
                .build();

        node1 = nodeRepository.save(node1);
        node2 = nodeRepository.save(node2);

        // Create edge
        Edge edge = Edge.builder()
                .source(node1)
                .target(node2)
                .weight(1.2)
                .map(map)
                .build();

        edge = edgeRepository.save(edge);

        // Verify relationships
        Map retrievedMap = mapRepository.findById(map.getId()).orElseThrow();
        assertThat(retrievedMap).isNotNull();

        Node retrievedNode1 = nodeRepository.findById(node1.getId()).orElseThrow();
        Node retrievedNode2 = nodeRepository.findById(node2.getId()).orElseThrow();

        // Test node properties
        assertThat(retrievedNode1.getMap().getId()).isEqualTo(map.getId());
        assertThat(retrievedNode1.getTerrainType()).isEqualTo(TerrainType.PLAIN);

        // Test edge properties
        Edge retrievedEdge = edgeRepository.findById(edge.getId()).orElseThrow();
        assertThat(retrievedEdge.getSource().getId()).isEqualTo(node1.getId());
        assertThat(retrievedEdge.getTarget().getId()).isEqualTo(node2.getId());
        assertThat(retrievedEdge.getWeight()).isEqualTo(1.2);
    }
}
