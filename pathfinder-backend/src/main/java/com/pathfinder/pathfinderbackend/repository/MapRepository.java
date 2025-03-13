package com.pathfinder.pathfinderbackend.repository;


import com.pathfinder.pathfinderbackend.model.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<Map, Long> {
    List<Map> findByOrderByCreatedAtDesc();
}
