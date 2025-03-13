package com.pathfinder.pathfinderbackend.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int width;

    @Column(nullable = false)
    private int height;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private Set<Node> nodes = new HashSet<>();

    @OneToMany(mappedBy = "map", cascade = CascadeType.ALL)
    private Set<Edge> edges = new HashSet<>();
}