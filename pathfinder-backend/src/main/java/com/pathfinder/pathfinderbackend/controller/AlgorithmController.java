package com.pathfinder.pathfinderbackend.controller;


import com.pathfinder.pathfinderbackend.dto.AlgorithmResultDto;
import com.pathfinder.pathfinderbackend.dto.ExecuteAlgorithmRequest;
import com.pathfinder.pathfinderbackend.model.AlgorithmResult;
import com.pathfinder.pathfinderbackend.service.AlgorithmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/algorithms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AlgorithmController {

    private final AlgorithmService algorithmService;

    @PostMapping("/maps/{mapId}")
    public ResponseEntity<AlgorithmResultDto> executeAlgorithm(
            @PathVariable Long mapId,
            @Valid @RequestBody ExecuteAlgorithmRequest request) {

        try {
            AlgorithmResult result = algorithmService.executeAlgorithm(
                    mapId,
                    request.getStartNodeId(),
                    request.getEndNodeId(),
                    request.getAlgorithmType()
            );

            return ResponseEntity.ok(AlgorithmResultDto.fromEntity(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/maps/{mapId}")
    public ResponseEntity<List<AlgorithmResultDto>> getResultsByMap(@PathVariable Long mapId) {
        try {
            List<AlgorithmResultDto> results = algorithmService.getResultsByMap(mapId).stream()
                    .map(AlgorithmResultDto::fromEntity)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{resultId}")
    public ResponseEntity<AlgorithmResultDto> getResultById(@PathVariable Long resultId) {
        Optional<AlgorithmResult> resultOpt = algorithmService.getResultById(resultId);

        return resultOpt.map(algorithmResult -> ResponseEntity.ok(AlgorithmResultDto.fromEntity(algorithmResult))).orElseGet(() -> ResponseEntity.notFound().build());

    }
}
