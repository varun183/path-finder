import { Container, Title, Text, Skeleton } from "@mantine/core";

const HomePage: React.FC = () => {
  return (
    <Container size="lg" py="xl">
      <Title order={1} mb="md">
        PathFinder
      </Title>
      <Text mb="xl">
        Welcome to PathFinder, a platform for visualizing and analyzing
        pathfinding algorithms.
      </Text>
      <Skeleton height={200} radius="md" mb="md" />
      <Skeleton height={50} radius="md" />
    </Container>
  );
};

export default HomePage;
