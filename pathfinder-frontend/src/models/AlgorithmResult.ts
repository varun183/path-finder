import { AlgorithmType } from "./AlgorithmType";

export interface AlgorithmResult {
  id: number;
  mapId: number;
  algorithmType: AlgorithmType;
  executionTime: string;
  executionDurationMs: number;
  memoryUsageBytes: number;
  nodesExplored: number;
  pathLength: number;
  pathFound: boolean;
  startNodeId: number;
  endNodeId: number;
  pathNodeIds: number[];
}
