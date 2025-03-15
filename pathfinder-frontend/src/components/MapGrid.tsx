import { Box, Group, ScrollArea } from "@mantine/core";
import GridCell from "./GridCell";
import { MapDetails } from "../models/MapDetails";
import { Node } from "../models/Node";

interface MapGridProps {
  mapDetails: MapDetails;
  startNodeId: number | null;
  endNodeId: number | null;
  pathNodeIds: number[];
  onNodeClick: (node: Node) => void;
}

const MapGrid: React.FC<MapGridProps> = ({
  mapDetails,
  startNodeId,
  endNodeId,
  pathNodeIds,
  onNodeClick,
}) => {
  const { map, nodes } = mapDetails;

  // Create a 2D grid from nodes
  const createGrid = () => {
    const grid: Node[][] = Array(map.height)
      .fill(null)
      .map(() => Array(map.width).fill(null));

    nodes.forEach((node) => {
      grid[node.y][node.x] = node;
    });

    return grid;
  };

  const grid = createGrid();

  return (
    <ScrollArea>
      <Box
        py="md"
        style={{
          border: "1px solid #dee2e6",
          display: "inline-block",
          background: "white",
        }}
      >
        {grid.map((row, rowIndex) => (
          <Group key={rowIndex} gap={0}>
            {row.map((node, colIndex) => (
              <GridCell
                key={`${rowIndex}-${colIndex}`}
                node={node}
                isStart={node.id === startNodeId}
                isEnd={node.id === endNodeId}
                isPath={pathNodeIds.includes(node.id)}
                onClick={onNodeClick}
              />
            ))}
          </Group>
        ))}
      </Box>
    </ScrollArea>
  );
};

export default MapGrid;
