package com.pathfinder.pathfinderbackend.service;


import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import com.pathfinder.pathfinderbackend.repository.EdgeRepository;
import com.pathfinder.pathfinderbackend.repository.MapRepository;
import com.pathfinder.pathfinderbackend.repository.NodeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MapGeneratorServiceTest {

    @Autowired
    private MapGeneratorService mapGeneratorService;

    @Autowired
    private MapRepository mapRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Test
    @Transactional
    public void shouldGenerateRandomMap() {
        // Act
        Map map = mapGeneratorService.generateRandomMap("Test Random Map", "A randomly generated map", 10, 10, 0.2);

        // Assert
        assertThat(map).isNotNull();
        assertThat(map.getId()).isNotNull();
        assertThat(map.getName()).isEqualTo("Test Random Map");

        List<Node> nodes = nodeRepository.findByMap(map);
        assertThat(nodes).hasSize(10 * 10); // 10x10 grid = 100 nodes

        var edges = edgeRepository.findByMap(map);
        assertThat(edges).isNotEmpty(); // Should have some edges

        // Verify each node has correct coordinates
        boolean hasAllCoordinates = true;
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                final int finalX = x;
                final int finalY = y;

                boolean foundCoordinate = nodes.stream()
                        .anyMatch(n -> n.getX() == finalX && n.getY() == finalY);
                if (!foundCoordinate) {
                    hasAllCoordinates = false;
                    break;
                }
            }
        }
        assertThat(hasAllCoordinates).isTrue();
    }
}
