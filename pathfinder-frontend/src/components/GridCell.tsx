import { Box, Tooltip } from "@mantine/core";
import { Node } from "../models/Node";
import { getTerrainColor, getTerrainName } from "../models/TerrainType";

interface GridCellProps {
  node: Node;
  isStart: boolean;
  isEnd: boolean;
  isPath: boolean;
  onClick: (node: Node) => void;
}

const GridCell: React.FC<GridCellProps> = ({
  node,
  isStart,
  isEnd,
  isPath,
  onClick,
}) => {
  const terrainColor = getTerrainColor(node.terrainType);

  let cellColor = terrainColor;
  let borderColor = "#dee2e6";

  if (isStart) {
    cellColor = "#40c057";
    borderColor = "#2b9348";
  } else if (isEnd) {
    cellColor = "#fa5252";
    borderColor = "#e03131";
  } else if (isPath) {
    cellColor = "#ae3ec9";
    borderColor = "#9c36b5";
  }

  return (
    <Tooltip
      label={`(${node.x}, ${node.y}) - ${getTerrainName(node.terrainType)}`}
      position="top"
      withArrow
    >
      <Box
        w={30}
        h={30}
        bg={cellColor}
        style={{
          border: `1px solid ${borderColor}`,
          cursor: "pointer",
          transition: "all 0.2s ease",
        }}
        onClick={() => onClick(node)}
        onMouseEnter={(e) => {
          e.currentTarget.style.opacity = "0.8";
          e.currentTarget.style.transform = "scale(1.05)";
        }}
        onMouseLeave={(e) => {
          e.currentTarget.style.opacity = "1";
          e.currentTarget.style.transform = "scale(1)";
        }}
      />
    </Tooltip>
  );
};

export default GridCell;
