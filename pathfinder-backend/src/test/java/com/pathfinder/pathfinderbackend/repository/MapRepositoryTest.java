package com.pathfinder.pathfinderbackend.repository;



import com.pathfinder.pathfinderbackend.model.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MapRepositoryTest {

    @Autowired
    private MapRepository mapRepository;

    @Test
    public void shouldSaveAndRetrieveMap() {
        // Create map
        Map map = Map.builder()
                .name("Test Map")
                .description("This is a test map")
                .width(10)
                .height(10)
                .build();

        // Save map
        Map savedMap = mapRepository.save(map);

        // Verify save
        assertThat(savedMap.getId()).isNotNull();

        // Retrieve by ID
        Map retrievedMap = mapRepository.findById(savedMap.getId()).orElse(null);

        // Verify retrieval
        assertThat(retrievedMap).isNotNull();
        assertThat(retrievedMap.getName()).isEqualTo("Test Map");
        assertThat(retrievedMap.getWidth()).isEqualTo(10);
        assertThat(retrievedMap.getHeight()).isEqualTo(10);
    }
}