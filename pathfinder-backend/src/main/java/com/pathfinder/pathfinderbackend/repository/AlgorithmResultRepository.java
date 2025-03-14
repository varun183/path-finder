package com.pathfinder.pathfinderbackend.repository;

import com.pathfinder.pathfinderbackend.model.AlgorithmResult;
import com.pathfinder.pathfinderbackend.model.AlgorithmType;
import com.pathfinder.pathfinderbackend.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmResultRepository extends JpaRepository<AlgorithmResult, Long> {
    List<AlgorithmResult> findByMap(Map map);
    List<AlgorithmResult> findByMapAndAlgorithmType(Map map, AlgorithmType algorithmType);
    List<AlgorithmResult> findByMapOrderByExecutionTimeDesc(Map map);
}
