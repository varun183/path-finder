CREATE TABLE map (
                     id BIGSERIAL PRIMARY KEY,
                     name VARCHAR(255) NOT NULL,
                     description TEXT,
                     width INTEGER NOT NULL,
                     height INTEGER NOT NULL,
                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TYPE terrain_type AS ENUM (
    'PLAIN',
    'GRASS',
    'FOREST',
    'MOUNTAIN',
    'WATER',
    'OBSTACLE'
);

CREATE TABLE node (
                      id BIGSERIAL PRIMARY KEY,
                      x INTEGER NOT NULL,
                      y INTEGER NOT NULL,
                      terrain_type terrain_type NOT NULL,
                      map_id BIGINT NOT NULL,
                      CONSTRAINT fk_map FOREIGN KEY (map_id) REFERENCES map (id) ON DELETE CASCADE,
                      CONSTRAINT unique_node_coordinates UNIQUE (map_id, x, y)
);

CREATE TABLE edge (
                      id BIGSERIAL PRIMARY KEY,
                      source_id BIGINT NOT NULL,
                      target_id BIGINT NOT NULL,
                      weight DOUBLE PRECISION NOT NULL,
                      map_id BIGINT NOT NULL,
                      CONSTRAINT fk_source FOREIGN KEY (source_id) REFERENCES node (id) ON DELETE CASCADE,
                      CONSTRAINT fk_target FOREIGN KEY (target_id) REFERENCES node (id) ON DELETE CASCADE,
                      CONSTRAINT fk_map_edge FOREIGN KEY (map_id) REFERENCES map (id) ON DELETE CASCADE,
                      CONSTRAINT unique_edge UNIQUE (source_id, target_id, map_id)
);

CREATE TYPE algorithm_type AS ENUM (
    'BFS',
    'DFS',
    'DIJKSTRA',
    'A_STAR'
);

CREATE TABLE algorithm_result (
                                  id BIGSERIAL PRIMARY KEY,
                                  map_id BIGINT NOT NULL,
                                  algorithm_type algorithm_type NOT NULL,
                                  execution_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  execution_duration_ms BIGINT NOT NULL,
                                  memory_usage_bytes BIGINT NOT NULL,
                                  nodes_explored INTEGER NOT NULL,
                                  path_length DOUBLE PRECISION NOT NULL,
                                  path_found BOOLEAN NOT NULL,
                                  start_node_id BIGINT NOT NULL,
                                  end_node_id BIGINT NOT NULL,
                                  CONSTRAINT fk_map_result FOREIGN KEY (map_id) REFERENCES map (id) ON DELETE CASCADE,
                                  CONSTRAINT fk_start_node FOREIGN KEY (start_node_id) REFERENCES node (id),
                                  CONSTRAINT fk_end_node FOREIGN KEY (end_node_id) REFERENCES node (id)
);

CREATE TABLE algorithm_result_path (
                                       result_id BIGINT NOT NULL,
                                       node_id BIGINT NOT NULL,
                                       position INTEGER NOT NULL,
                                       PRIMARY KEY (result_id, position),
                                       CONSTRAINT fk_result FOREIGN KEY (result_id) REFERENCES algorithm_result (id) ON DELETE CASCADE,
                                       CONSTRAINT fk_node FOREIGN KEY (node_id) REFERENCES node (id)
);