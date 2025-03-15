export enum AlgorithmType {
  BFS = "BFS",
  DFS = "DFS",
  DIJKSTRA = "DIJKSTRA",
  A_STAR = "A_STAR",
}

export const algorithmNames: Record<AlgorithmType, string> = {
  [AlgorithmType.BFS]: "Breadth-First Search",
  [AlgorithmType.DFS]: "Depth-First Search",
  [AlgorithmType.DIJKSTRA]: "Dijkstra's Algorithm",
  [AlgorithmType.A_STAR]: "A* Algorithm",
};
