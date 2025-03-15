export enum TerrainType {
  PLAIN = "PLAIN",
  GRASS = "GRASS",
  FOREST = "FOREST",
  MOUNTAIN = "MOUNTAIN",
  WATER = "WATER",
  OBSTACLE = "OBSTACLE",
}

export const getTerrainColor = (terrainType: TerrainType): string => {
  switch (terrainType) {
    case TerrainType.PLAIN:
      return "#f0f0f0";
    case TerrainType.GRASS:
      return "#90ee90";
    case TerrainType.FOREST:
      return "#228b22";
    case TerrainType.MOUNTAIN:
      return "#a9a9a9";
    case TerrainType.WATER:
      return "#add8e6";
    case TerrainType.OBSTACLE:
      return "#4a4a4a";
    default:
      return "#f0f0f0";
  }
};

export const getTerrainName = (terrainType: TerrainType): string => {
  return terrainType.charAt(0) + terrainType.slice(1).toLowerCase();
};
