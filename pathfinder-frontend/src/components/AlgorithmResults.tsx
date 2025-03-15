import {
  Box,
  Group,
  Text,
  Badge,
  Paper,
  SimpleGrid,
  Divider,
} from "@mantine/core";
import { AlgorithmResult } from "../models/AlgorithmResult";
import { algorithmNames } from "../models/AlgorithmType";

interface AlgorithmResultsProps {
  result: AlgorithmResult | null;
}

const formatNumber = (num: number): string => {
  return num.toLocaleString();
};

const formatBytes = (bytes: number): string => {
  if (bytes < 1024) return bytes + " bytes";
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + " KB";
  return (bytes / (1024 * 1024)).toFixed(2) + " MB";
};

const formatTime = (ms: number): string => {
  if (ms < 1) return `${(ms * 1000).toFixed(2)} μs`;
  if (ms < 1000) return `${ms.toFixed(2)} ms`;
  return `${(ms / 1000).toFixed(2)} s`;
};

const AlgorithmResults: React.FC<AlgorithmResultsProps> = ({ result }) => {
  if (!result) {
    return (
      <Box p="md" mt="md">
        <Text>No results to display. Execute an algorithm first.</Text>
      </Box>
    );
  }

  return (
    <Paper p="md" withBorder mt="md">
      <Group mb="md">
        <Text fw={700} size="lg">
          Algorithm Results
        </Text>
        {result.pathFound ? (
          <Badge color="green" size="lg">
            Path Found
          </Badge>
        ) : (
          <Badge color="red" size="lg">
            No Path Found
          </Badge>
        )}
      </Group>

      <SimpleGrid cols={{ base: 1, sm: 2, md: 4 }} mb="md">
        <Box>
          <Text size="sm" c="dimmed">
            Execution Time
          </Text>
          <Text fw={600}>{formatTime(result.executionDurationMs)}</Text>
        </Box>
        <Box>
          <Text size="sm" c="dimmed">
            Memory Usage
          </Text>
          <Text fw={600}>{formatBytes(result.memoryUsageBytes)}</Text>
        </Box>
        <Box>
          <Text size="sm" c="dimmed">
            Nodes Explored
          </Text>
          <Text fw={600}>{formatNumber(result.nodesExplored)}</Text>
        </Box>
        <Box>
          <Text size="sm" c="dimmed">
            Path Length
          </Text>
          <Text fw={600}>{result.pathLength.toFixed(2)}</Text>
        </Box>
      </SimpleGrid>

      <Divider my="sm" />

      <Group>
        <Box>
          <Text size="sm" c="dimmed">
            Algorithm
          </Text>
          <Text>{algorithmNames[result.algorithmType]}</Text>
        </Box>
        <Box>
          <Text size="sm" c="dimmed">
            Path Nodes
          </Text>
          <Text>{result.pathNodeIds.length}</Text>
        </Box>
      </Group>
    </Paper>
  );
};

export default AlgorithmResults;
