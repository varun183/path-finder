package com.pathfinder.pathfinderbackend.repository;


import com.pathfinder.pathfinderbackend.model.Map;
import com.pathfinder.pathfinderbackend.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> findByMap(Map map);

    @Query("SELECT n FROM Node n WHERE n.map.id = :mapId AND n.x = :x AND n.y = :y")
    Optional<Node> findByMapAndCoordinates(Long mapId, int x, int y);
}
