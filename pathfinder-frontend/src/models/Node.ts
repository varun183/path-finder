import { TerrainType } from "./TerrainType";

export interface Node {
  id: number;
  x: number;
  y: number;
  terrainType: TerrainType;
}
