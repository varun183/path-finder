
ALTER TABLE node ALTER COLUMN terrain_type TYPE varchar;

ALTER TABLE node ADD CONSTRAINT check_valid_terrain_type
    CHECK (terrain_type IN ('PLAIN', 'GRASS', 'FOREST', 'MOUNTAIN', 'WATER', 'OBSTACLE'));