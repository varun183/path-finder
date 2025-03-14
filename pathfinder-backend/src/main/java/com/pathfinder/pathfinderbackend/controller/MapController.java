package com.pathfinder.pathfinderbackend.controller;



import com.pathfinder.pathfinderbackend.dto.CreateMapRequest;
import com.pathfinder.pathfinderbackend.dto.GenerateMapRequest;
import com.pathfinder.pathfinderbackend.dto.MapDetailsDto;
import com.pathfinder.pathfinderbackend.dto.MapDto;
import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.service.MapGeneratorService;
import com.pathfinder.pathfinderbackend.service.MapService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maps")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MapController {

    private final MapService mapService;
    private final MapGeneratorService mapGeneratorService;


    @GetMapping
    public ResponseEntity<List<MapDto>> getAllMaps() {
        List<MapDto> maps = mapService.getAllMaps().stream()
                .map(MapDto::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.ok(maps);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MapDetailsDto> getMapById(@PathVariable Long id) {
        Optional<Map> mapOpt = mapService.getMapById(id);

        return mapOpt.map(map -> ResponseEntity.ok(MapDetailsDto.fromEntity(map))).orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<MapDto> createMap(@Valid @RequestBody CreateMapRequest request) {
        Map map = mapService.createMap(
                request.getName(),
                request.getDescription(),
                request.getWidth(),
                request.getHeight()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(MapDto.fromEntity(map));
    }

    @PostMapping("/generate")
    public ResponseEntity<MapDto> generateMap(@Valid @RequestBody GenerateMapRequest request) {
        Map map = mapGeneratorService.generateRandomMap(
                request.getName(),
                request.getDescription(),
                request.getWidth(),
                request.getHeight(),
                request.getObstacleDensity()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(MapDto.fromEntity(map));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMap(@PathVariable Long id) {
        Optional<Map> mapOpt = mapService.getMapById(id);

        if (mapOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        mapService.deleteMap(id);
        return ResponseEntity.noContent().build();
    }
}
