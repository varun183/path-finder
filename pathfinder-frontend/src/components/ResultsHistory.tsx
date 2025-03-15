import { useState, useEffect } from "react";
import {
  Paper,
  Title,
  Table,
  Badge,
  Button,
  Text,
  Alert,
  LoadingOverlay,
} from "@mantine/core";
import { IconAlertCircle } from "@tabler/icons-react";
import { AlgorithmResult } from "../models/AlgorithmResult";
import { algorithmNames } from "../models/AlgorithmType";
import algorithmService from "../services/algorithmService";

interface ResultsHistoryProps {
  mapId: number;
  onResultSelect: (result: AlgorithmResult) => void;
}

const formatTime = (ms: number): string => {
  if (ms < 1) return `${(ms * 1000).toFixed(2)} μs`;
  if (ms < 1000) return `${ms.toFixed(2)} ms`;
  return `${(ms / 1000).toFixed(2)} s`;
};

const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr);
  return date.toLocaleString();
};

const ResultsHistory: React.FC<ResultsHistoryProps> = ({
  mapId,
  onResultSelect,
}) => {
  const [results, setResults] = useState<AlgorithmResult[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchResults = async () => {
      try {
        setLoading(true);
        const data = await algorithmService.getResultsByMap(mapId);
        setResults(data);
        setError(null);
      } catch (err) {
        console.error("Error fetching results:", err);
        setError("Failed to load algorithm results.");
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [mapId]);

  if (loading) {
    return (
      <Paper p="md" withBorder pos="relative" h={200}>
        <LoadingOverlay
          visible={true}
          overlayProps={{ radius: "sm", blur: 2 }}
        />
      </Paper>
    );
  }

  if (error) {
    return (
      <Alert icon={<IconAlertCircle size="1rem" />} title="Error" color="red">
        {error}
      </Alert>
    );
  }

  if (results.length === 0) {
    return (
      <Paper p="md" withBorder>
        <Text>
          No algorithm executions yet. Try running an algorithm first.
        </Text>
      </Paper>
    );
  }

  return (
    <Paper p="md" withBorder>
      <Title order={3} mb="md">
        Algorithm Execution History
      </Title>

      <Table striped highlightOnHover>
        <Table.Thead>
          <Table.Tr>
            <Table.Th>Algorithm</Table.Th>
            <Table.Th>Time</Table.Th>
            <Table.Th>Nodes Explored</Table.Th>
            <Table.Th>Path</Table.Th>
            <Table.Th>Executed</Table.Th>
            <Table.Th>Action</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>
          {results.map((result) => (
            <Table.Tr key={result.id}>
              <Table.Td>{algorithmNames[result.algorithmType]}</Table.Td>
              <Table.Td>{formatTime(result.executionDurationMs)}</Table.Td>
              <Table.Td>{result.nodesExplored.toLocaleString()}</Table.Td>
              <Table.Td>
                {result.pathFound ? (
                  <Badge color="green">
                    Found ({result.pathLength.toFixed(2)})
                  </Badge>
                ) : (
                  <Badge color="red">Not Found</Badge>
                )}
              </Table.Td>
              <Table.Td>{formatDate(result.executionTime)}</Table.Td>
              <Table.Td>
                <Button
                  variant="light"
                  size="xs"
                  onClick={() => onResultSelect(result)}
                >
                  View
                </Button>
              </Table.Td>
            </Table.Tr>
          ))}
        </Table.Tbody>
      </Table>
    </Paper>
  );
};

export default ResultsHistory;
