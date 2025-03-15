import api from "./api";
import { Map } from "../models/Map";
import { MapDetails } from "../models/MapDetails";

export interface GenerateMapRequest {
  name: string;
  description: string;
  width: number;
  height: number;
  obstacleDensity: number;
}

const mapService = {
  getAllMaps: async (): Promise<Map[]> => {
    const response = await api.get<Map[]>("/maps");
    return response.data;
  },

  getMapById: async (id: number): Promise<MapDetails> => {
    const response = await api.get<MapDetails>(`/maps/${id}`);
    return response.data;
  },

  generateMap: async (data: GenerateMapRequest): Promise<Map> => {
    const response = await api.post<Map>("/maps/generate", data);
    return response.data;
  },

  deleteMap: async (id: number): Promise<void> => {
    await api.delete(`/maps/${id}`);
  },
};

export default mapService;
