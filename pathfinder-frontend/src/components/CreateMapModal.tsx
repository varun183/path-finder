import {
  Modal,
  TextInput,
  Textarea,
  NumberInput,
  Stack,
  Button,
  Group,
  Slider,
  Text,
  Box,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { GenerateMapRequest } from "../services/mapService";

interface CreateMapModalProps {
  opened: boolean;
  onClose: () => void;
  onSubmit: (values: GenerateMapRequest) => Promise<void>;
  isLoading: boolean;
}

const CreateMapModal: React.FC<CreateMapModalProps> = ({
  opened,
  onClose,
  onSubmit,
  isLoading,
}) => {
  const form = useForm<GenerateMapRequest>({
    initialValues: {
      name: "",
      description: "",
      width: 20,
      height: 20,
      obstacleDensity: 0.2,
    },
    validate: {
      name: (value) =>
        value.length < 3 ? "Name must have at least 3 characters" : null,
      width: (value) =>
        value < 5
          ? "Width must be at least 5"
          : value > 50
          ? "Width cannot exceed 50"
          : null,
      height: (value) =>
        value < 5
          ? "Height must be at least 5"
          : value > 50
          ? "Height cannot exceed 50"
          : null,
    },
  });

  const handleSubmit = async (values: GenerateMapRequest) => {
    await onSubmit(values);
    form.reset();
  };

  return (
    <Modal
      opened={opened}
      onClose={onClose}
      title="Create New Map"
      size="md"
      centered
    >
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Stack>
          <TextInput
            label="Map Name"
            placeholder="Enter map name"
            required
            {...form.getInputProps("name")}
          />

          <Textarea
            label="Description (Optional)"
            placeholder="Enter map description"
            {...form.getInputProps("description")}
          />

          <Group grow>
            <NumberInput
              label="Width"
              placeholder="Map width"
              min={5}
              max={50}
              required
              {...form.getInputProps("width")}
            />

            <NumberInput
              label="Height"
              placeholder="Map height"
              min={5}
              max={50}
              required
              {...form.getInputProps("height")}
            />
          </Group>

          <Box>
            <Text size="sm" fw={500} mb={5}>
              Obstacle Density: {(form.values.obstacleDensity * 100).toFixed(0)}
              %
            </Text>
            <Slider
              min={0}
              max={0.5}
              step={0.01}
              label={(value) => `${(value * 100).toFixed(0)}%`}
              {...form.getInputProps("obstacleDensity")}
            />
          </Box>

          <Group justify="flex-end" mt="md">
            <Button variant="light" onClick={onClose}>
              Cancel
            </Button>
            <Button type="submit" loading={isLoading}>
              Create Map
            </Button>
          </Group>
        </Stack>
      </form>
    </Modal>
  );
};

export default CreateMapModal;
