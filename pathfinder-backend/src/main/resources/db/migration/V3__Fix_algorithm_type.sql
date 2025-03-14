-- Drop the enum type constraint and convert to varchar
ALTER TABLE algorithm_result
ALTER COLUMN algorithm_type TYPE VARCHAR(20) USING algorithm_type::text;

-- Drop the custom enum type
DROP TYPE IF EXISTS algorithm_type CASCADE;

-- Add a check constraint to maintain data integrity
ALTER TABLE algorithm_result
    ADD CONSTRAINT algorithm_type_check
        CHECK (algorithm_type IN ('BFS', 'DFS', 'DIJKSTRA', 'A_STAR'));