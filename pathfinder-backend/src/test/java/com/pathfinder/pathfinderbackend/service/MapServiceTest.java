package com.pathfinder.pathfinderbackend.service;


import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.repository.MapRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MapServiceTest {

    @Mock
    private MapRepository mapRepository;

    @InjectMocks
    private MapService mapService;

    @Test
    public void shouldCreateMap() {
        // Arrange
        Map mapToSave = Map.builder()
                .name("Test Map")
                .description("Test Description")
                .width(10)
                .height(10)
                .build();

        Map savedMap = Map.builder()
                .id(1L)
                .name("Test Map")
                .description("Test Description")
                .width(10)
                .height(10)
                .build();

        when(mapRepository.save(any(Map.class))).thenReturn(savedMap);

        // Act
        Map result = mapService.createMap("Test Map", "Test Description", 10, 10);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Map");

        verify(mapRepository).save(any(Map.class));
    }

    @Test
    public void shouldGetAllMaps() {
        // Arrange
        List<Map> maps = List.of(
                Map.builder().id(1L).name("Map 1").build(),
                Map.builder().id(2L).name("Map 2").build()
        );

        when(mapRepository.findByOrderByCreatedAtDesc()).thenReturn(maps);

        // Act
        List<Map> result = mapService.getAllMaps();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Map 1");
        assertThat(result.get(1).getName()).isEqualTo("Map 2");

        verify(mapRepository).findByOrderByCreatedAtDesc();
    }
}