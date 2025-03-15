import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  Container,
  Title,
  Text,
  Button,
  Stack,
  Card,
  Group,
  Badge,
  LoadingOverlay,
  Box,
  Alert,
} from "@mantine/core";
import { IconAlertCircle, IconPlus } from "@tabler/icons-react";
import { useDisclosure } from "@mantine/hooks";
import { notifications } from "@mantine/notifications";
import mapService, { GenerateMapRequest } from "../services/mapService";
import { Map } from "../models/Map";
import CreateMapModal from "../components/CreateMapModal";

const HomePage: React.FC = () => {
  const navigate = useNavigate();
  const [maps, setMaps] = useState<Map[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const [creating, setCreating] = useState<boolean>(false);
  const [opened, { open, close }] = useDisclosure(false);

  useEffect(() => {
    fetchMaps();
  }, []);

  const fetchMaps = async () => {
    try {
      setLoading(true);
      const data = await mapService.getAllMaps();
      setMaps(data);
      setError(null);
    } catch (err) {
      console.error("Error fetching maps:", err);
      setError("Failed to load maps. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateMap = async (data: GenerateMapRequest) => {
    try {
      setCreating(true);
      const newMap = await mapService.generateMap(data);
      setMaps([newMap, ...maps]);
      notifications.show({
        title: "Success",
        message: "Map created successfully",
        color: "green",
      });
      close();
    } catch (err) {
      console.error("Error creating map:", err);
      notifications.show({
        title: "Error",
        message: "Failed to create map. Please try again.",
        color: "red",
      });
    } finally {
      setCreating(false);
    }
  };

  const handleDeleteMap = async (id: number, name: string) => {
    if (!window.confirm(`Are you sure you want to delete the map "${name}"?`)) {
      return;
    }

    try {
      await mapService.deleteMap(id);
      setMaps(maps.filter((map) => map.id !== id));
      notifications.show({
        title: "Success",
        message: "Map deleted successfully",
        color: "green",
      });
    } catch (err) {
      console.error("Error deleting map:", err);
      notifications.show({
        title: "Error",
        message: "Failed to delete map. Please try again.",
        color: "red",
      });
    }
  };

  return (
    <Container size="lg" py="xl">
      <Group justify="space-between" mb="xl">
        <Title order={1}>PathFinder</Title>
        <Button leftSection={<IconPlus size={16} />} onClick={open}>
          Create New Map
        </Button>
      </Group>

      <Text mb="xl">
        Welcome to PathFinder, a platform for visualizing and analyzing
        pathfinding algorithms. Create a map to get started or select an
        existing one.
      </Text>

      <Box pos="relative">
        <LoadingOverlay
          visible={loading}
          overlayProps={{ radius: "sm", blur: 2 }}
        />

        {error && (
          <Alert icon={<IconAlertCircle />} title="Error" color="red" mb="md">
            {error}
          </Alert>
        )}

        {!loading && maps.length === 0 && !error && (
          <Alert title="No maps available" color="blue" mb="md">
            No maps available. Click the "Create New Map" button to get started.
          </Alert>
        )}

        <Stack>
          {maps.map((map) => (
            <Card key={map.id} shadow="sm" padding="md" radius="md" withBorder>
              <Group justify="space-between" mb="xs">
                <Title order={3}>{map.name}</Title>
                <Text size="sm" c="dimmed">
                  Created: {new Date(map.createdAt).toLocaleDateString()}
                </Text>
              </Group>

              <Text lineClamp={2} mb="md">
                {map.description || "No description provided."}
              </Text>

              <Group mb="md">
                <Badge color="blue">
                  Size: {map.width} x {map.height}
                </Badge>
                <Badge color="teal">Nodes: {map.nodeCount}</Badge>
                <Badge color="grape">Edges: {map.edgeCount}</Badge>
              </Group>

              <Group justify="flex-end">
                <Button
                  variant="light"
                  color="red"
                  onClick={() => handleDeleteMap(map.id, map.name)}
                >
                  Delete
                </Button>
                <Button onClick={() => navigate(`/maps/${map.id}`)}>
                  View Map
                </Button>
              </Group>
            </Card>
          ))}
        </Stack>
      </Box>

      <CreateMapModal
        opened={opened}
        onClose={close}
        onSubmit={handleCreateMap}
        isLoading={creating}
      />
    </Container>
  );
};

export default HomePage;
