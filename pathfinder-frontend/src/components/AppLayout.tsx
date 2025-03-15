import { ReactNode } from "react";
import { AppShell, Container } from "@mantine/core";
import AppHeader from "./AppHeader";

interface AppLayoutProps {
  children: ReactNode;
}

const AppLayout: React.FC<AppLayoutProps> = ({ children }) => {
  return (
    <AppShell header={{ height: 60 }} padding="md">
      <AppShell.Header>
        <AppHeader />
      </AppShell.Header>

      <AppShell.Main>
        <Container size="xl" py="md">
          {children}
        </Container>
      </AppShell.Main>
    </AppShell>
  );
};

export default AppLayout;
