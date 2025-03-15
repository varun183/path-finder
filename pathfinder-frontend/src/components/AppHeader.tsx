import { Group, Title, Button, Container } from "@mantine/core";
import { useNavigate, useLocation } from "react-router-dom";
import { IconHome } from "@tabler/icons-react";

const AppHeader: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  return (
    <Container size="xl" h="100%">
      <Group h="100%" justify="space-between">
        <Group>
          <Title
            order={3}
            style={{ cursor: "pointer" }}
            onClick={() => navigate("/")}
          >
            PathFinder
          </Title>
        </Group>

        {location.pathname !== "/" && (
          <Button
            variant="subtle"
            leftSection={<IconHome size="1rem" />}
            onClick={() => navigate("/")}
          >
            Home
          </Button>
        )}
      </Group>
    </Container>
  );
};

export default AppHeader;
