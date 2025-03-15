import api from "./api";
import { AlgorithmResult } from "../models/AlgorithmResult";
import { AlgorithmType } from "../models/AlgorithmType";

export interface ExecuteAlgorithmRequest {
  startNodeId: number;
  endNodeId: number;
  algorithmType: AlgorithmType;
}

const algorithmService = {
  executeAlgorithm: async (
    mapId: number,
    data: ExecuteAlgorithmRequest
  ): Promise<AlgorithmResult> => {
    const response = await api.post<AlgorithmResult>(
      `/algorithms/maps/${mapId}`,
      data
    );
    return response.data;
  },

  getResultsByMap: async (mapId: number): Promise<AlgorithmResult[]> => {
    const response = await api.get<AlgorithmResult[]>(
      `/algorithms/maps/${mapId}`
    );
    return response.data;
  },

  getResultById: async (resultId: number): Promise<AlgorithmResult> => {
    const response = await api.get<AlgorithmResult>(`/algorithms/${resultId}`);
    return response.data;
  },
};

export default algorithmService;
