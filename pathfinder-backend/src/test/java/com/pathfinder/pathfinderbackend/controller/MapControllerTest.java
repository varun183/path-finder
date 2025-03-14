package com.pathfinder.pathfinderbackend.controller;

import com.pathfinder.pathfinderbackend.dto.CreateMapRequest;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.service.MapGeneratorService;
import com.pathfinder.pathfinderbackend.service.MapService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class MapControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MapService mapService;

    @Mock
    private  MapGeneratorService mapGeneratorService;


    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        MapController mapController = new MapController(mapService,mapGeneratorService);
        mockMvc = MockMvcBuilders.standaloneSetup(mapController).build();
    }

    @Test
    public void shouldCreateMap() throws Exception {
        // Arrange
        CreateMapRequest request = new CreateMapRequest();
        request.setName("Test Map");
        request.setDescription("Test Description");
        request.setWidth(10);
        request.setHeight(10);

        Map createdMap = Map.builder()
                .id(1L)
                .name("Test Map")
                .description("Test Description")
                .width(10)
                .height(10)
                .createdAt(LocalDateTime.now())
                .build();

        when(mapService.createMap(anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn(createdMap);

        // Act & Assert
        mockMvc.perform(post("/api/maps")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Map"))
                .andExpect(jsonPath("$.width").value(10))
                .andExpect(jsonPath("$.height").value(10));
    }

    @Test
    public void shouldGetAllMaps() throws Exception {
        // Arrange
        Map map1 = Map.builder()
                .id(1L)
                .name("Map 1")
                .width(10)
                .height(10)
                .createdAt(LocalDateTime.now())
                .build();

        Map map2 = Map.builder()
                .id(2L)
                .name("Map 2")
                .width(20)
                .height(15)
                .createdAt(LocalDateTime.now())
                .build();

        when(mapService.getAllMaps()).thenReturn(List.of(map1, map2));

        // Act & Assert
        mockMvc.perform(get("/api/maps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Map 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Map 2"));
    }

    @Test
    public void shouldGetMapById() throws Exception {
        // Arrange
        Map map = Map.builder()
                .id(1L)
                .name("Map 1")
                .width(10)
                .height(10)
                .createdAt(LocalDateTime.now())
                .build();

        when(mapService.getMapById(1L)).thenReturn(Optional.of(map));

        // Act & Assert
        mockMvc.perform(get("/api/maps/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Map 1"));
    }

    @Test
    public void shouldReturn404WhenMapNotFound() throws Exception {
        // Arrange
        when(mapService.getMapById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/maps/999"))
                .andExpect(status().isNotFound());
    }
}