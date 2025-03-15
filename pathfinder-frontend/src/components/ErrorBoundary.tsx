import { Component, ErrorInfo, ReactNode } from "react";
import { Container, Title, Text, Button, Stack, Paper } from "@mantine/core";

interface Props {
  children: ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | null;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    error: null,
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error("Uncaught error:", error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return (
        <Container size="sm" py="xl">
          <Paper p="xl" withBorder shadow="md">
            <Stack>
              <Title order={2} c="red">
                Something went wrong
              </Title>
              <Text>An unexpected error occurred in the application.</Text>
              {this.state.error && (
                <Paper p="md" bg="red.0" withBorder>
                  <Text c="red.9" fw={500}>
                    {this.state.error.message}
                  </Text>
                </Paper>
              )}
              <Button onClick={() => (window.location.href = "/")} color="blue">
                Return to Home
              </Button>
            </Stack>
          </Paper>
        </Container>
      );
    }

    return this.props.children;
  }
}

export default ErrorBoundary;
