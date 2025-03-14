package com.pathfinder.pathfinderbackend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private Map map;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlgorithmType algorithmType;

    @CreationTimestamp
    private LocalDateTime executionTime;

    private long executionDurationMs;
    private long memoryUsageBytes;
    private int nodesExplored;
    private double pathLength;
    private boolean pathFound;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_node_id", nullable = false)
    private Node startNode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_node_id", nullable = false)
    private Node endNode;

    @ElementCollection
    @CollectionTable(
            name = "algorithm_result_path",
            joinColumns = @JoinColumn(name = "result_id")
    )
    @Column(name = "node_id")
    @OrderColumn(name = "position")
    private List<Long> pathNodeIds = new ArrayList<>();
}
