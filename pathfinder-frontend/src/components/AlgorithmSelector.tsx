import { Box, Button, Select, Group, Text } from "@mantine/core";
import { AlgorithmType, algorithmNames } from "../models/AlgorithmType";

interface AlgorithmSelectorProps {
  selectedAlgorithm: AlgorithmType;
  onAlgorithmChange: (algorithm: AlgorithmType) => void;
  onExecute: () => void;
  isExecuting: boolean;
  isDisabled: boolean;
}

const AlgorithmSelector: React.FC<AlgorithmSelectorProps> = ({
  selectedAlgorithm,
  onAlgorithmChange,
  onExecute,
  isExecuting,
  isDisabled,
}) => {
  const algorithmOptions = Object.values(AlgorithmType).map((type) => ({
    value: type,
    label: algorithmNames[type],
  }));

  return (
    <Box p="md" bg="gray.0" style={{ borderRadius: 8 }} mb="md">
      <Text fw={600} mb="sm">
        Select Algorithm
      </Text>
      <Group>
        <Select
          data={algorithmOptions}
          value={selectedAlgorithm}
          onChange={(value) =>
            value && onAlgorithmChange(value as AlgorithmType)
          }
          disabled={isDisabled}
          style={{ flex: 1 }}
        />
        <Button onClick={onExecute} loading={isExecuting} disabled={isDisabled}>
          Execute Algorithm
        </Button>
      </Group>
    </Box>
  );
};

export default AlgorithmSelector;
