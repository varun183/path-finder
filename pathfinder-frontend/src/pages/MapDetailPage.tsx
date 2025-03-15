import { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {
  Container,
  Title,
  Text,
  Button,
  Group,
  Box,
  Badge,
  Alert,
  LoadingOverlay,
  Paper,
} from "@mantine/core";
import { IconAlertCircle, IconArrowLeft } from "@tabler/icons-react";
import { notifications } from "@mantine/notifications";
import { MapDetails } from "../models/MapDetails";
import { Node } from "../models/Node";
import { AlgorithmType } from "../models/AlgorithmType";
import { AlgorithmResult } from "../models/AlgorithmResult";
import mapService from "../services/mapService";
import algorithmService from "../services/algorithmService";
import MapGrid from "../components/MapGrid";
import AlgorithmSelector from "../components/AlgorithmSelector";
import AlgorithmResults from "../components/AlgorithmResults";

const MapDetailPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [mapDetails, setMapDetails] = useState<MapDetails | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const [startNode, setStartNode] = useState<Node | null>(null);
  const [endNode, setEndNode] = useState<Node | null>(null);
  const [selectedAlgorithm, setSelectedAlgorithm] = useState<AlgorithmType>(
    AlgorithmType.A_STAR
  );
  const [isExecuting, setIsExecuting] = useState<boolean>(false);
  const [currentResult, setCurrentResult] = useState<AlgorithmResult | null>(
    null
  );

  useEffect(() => {
    const fetchMapDetails = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const data = await mapService.getMapById(Number(id));
        setMapDetails(data);
        setError(null);
      } catch (err) {
        console.error("Error fetching map details:", err);
        setError("Failed to load map details. Please try again later.");
        notifications.show({
          title: "Error",
          message: "Failed to load map details",
          color: "red",
        });
      } finally {
        setLoading(false);
      }
    };

    fetchMapDetails();
  }, [id]);

  const handleNodeClick = (node: Node) => {
    if (!startNode) {
      setStartNode(node);
      notifications.show({
        title: "Start Node Selected",
        message: `Start node set at (${node.x}, ${node.y})`,
        color: "green",
      });
    } else if (!endNode) {
      if (node.id === startNode.id) {
        notifications.show({
          title: "Invalid Selection",
          message: "Start and end nodes must be different",
          color: "orange",
        });
        return;
      }
      setEndNode(node);
      notifications.show({
        title: "End Node Selected",
        message: `End node set at (${node.x}, ${node.y})`,
        color: "green",
      });
    } else {
      // Reset selection
      setStartNode(node);
      setEndNode(null);
      setCurrentResult(null);
      notifications.show({
        title: "Selection Reset",
        message: `New start node set at (${node.x}, ${node.y})`,
        color: "blue",
      });
    }
  };

  const handleResetSelection = () => {
    setStartNode(null);
    setEndNode(null);
    setCurrentResult(null);
    notifications.show({
      title: "Selection Reset",
      message: "Start and end nodes have been cleared",
      color: "blue",
    });
  };

  const executeAlgorithm = async () => {
    if (!mapDetails || !startNode || !endNode) {
      notifications.show({
        title: "Cannot Execute",
        message: "Please select both start and end nodes first",
        color: "orange",
      });
      return;
    }

    setIsExecuting(true);

    try {
      const result = await algorithmService.executeAlgorithm(
        mapDetails.map.id,
        {
          startNodeId: startNode.id,
          endNodeId: endNode.id,
          algorithmType: selectedAlgorithm,
        }
      );

      setCurrentResult(result);

      notifications.show({
        title: "Algorithm Executed",
        message: result.pathFound
          ? `Path found with ${result.pathNodeIds.length} nodes`
          : "No path found between selected nodes",
        color: result.pathFound ? "green" : "orange",
      });
    } catch (err) {
      console.error("Error executing algorithm:", err);
      notifications.show({
        title: "Error",
        message: "Failed to execute algorithm. Please try again.",
        color: "red",
      });
    } finally {
      setIsExecuting(false);
    }
  };

  if (loading) {
    return (
      <Container size="xl" py="xl" pos="relative" h="80vh">
        <LoadingOverlay
          visible={true}
          overlayProps={{ radius: "sm", blur: 2 }}
        />
      </Container>
    );
  }

  if (error || !mapDetails) {
    return (
      <Container size="md" py="xl">
        <Alert
          icon={<IconAlertCircle size="1rem" />}
          title="Error Loading Map"
          color="red"
          mb="md"
        >
          {error || "Unable to load map details."}
        </Alert>
        <Button
          leftSection={<IconArrowLeft size="1rem" />}
          onClick={() => navigate("/")}
        >
          Back to Maps
        </Button>
      </Container>
    );
  }

  return (
    <Container size="xl" py="xl">
      <Group justify="space-between" mb="md">
        <Group>
          <Button
            variant="subtle"
            leftSection={<IconArrowLeft size="1rem" />}
            onClick={() => navigate("/")}
          >
            Back
          </Button>
          <Title order={2}>{mapDetails.map.name}</Title>
        </Group>
      </Group>

      <Text mb="md">
        {mapDetails.map.description || "No description provided."}
      </Text>

      <Group mb="xl">
        <Badge color="blue">
          Size: {mapDetails.map.width} x {mapDetails.map.height}
        </Badge>
        <Badge color="teal">Nodes: {mapDetails.map.nodeCount}</Badge>
        <Badge color="grape">Edges: {mapDetails.map.edgeCount}</Badge>
        <Badge color="orange">
          Created: {new Date(mapDetails.map.createdAt).toLocaleDateString()}
        </Badge>
      </Group>

      <Paper p="md" withBorder mb="md">
        <Group mb="md">
          <Text fw={700}>Node Selection:</Text>
          {!startNode ? (
            <Text c="dimmed">Click on a cell to select the start node</Text>
          ) : !endNode ? (
            <Group>
              <Badge color="green">
                Start: ({startNode.x}, {startNode.y})
              </Badge>
              <Text c="dimmed">Click on a cell to select the end node</Text>
            </Group>
          ) : (
            <Group>
              <Badge color="green">
                Start: ({startNode.x}, {startNode.y})
              </Badge>
              <Badge color="red">
                End: ({endNode.x}, {endNode.y})
              </Badge>
              <Button variant="subtle" size="xs" onClick={handleResetSelection}>
                Reset Selection
              </Button>
            </Group>
          )}
        </Group>

        <AlgorithmSelector
          selectedAlgorithm={selectedAlgorithm}
          onAlgorithmChange={setSelectedAlgorithm}
          onExecute={executeAlgorithm}
          isExecuting={isExecuting}
          isDisabled={!startNode || !endNode}
        />
      </Paper>

      {currentResult && <AlgorithmResults result={currentResult} />}

      <Box mt="md">
        <MapGrid
          mapDetails={mapDetails}
          startNodeId={startNode?.id || null}
          endNodeId={endNode?.id || null}
          pathNodeIds={currentResult?.pathNodeIds || []}
          onNodeClick={handleNodeClick}
        />
      </Box>
    </Container>
  );
};

export default MapDetailPage;
