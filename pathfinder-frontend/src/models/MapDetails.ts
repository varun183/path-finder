import { Map } from "./Map";
import { Node } from "./Node";
import { Edge } from "./Edge";

export interface MapDetails {
  map: Map;
  nodes: Node[];
  edges: Edge[];
}
