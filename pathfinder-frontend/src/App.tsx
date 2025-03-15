import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { MantineProvider, createTheme } from "@mantine/core";
import { Notifications } from "@mantine/notifications";
import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";
import HomePage from "./pages/HomePage";
import MapDetailPage from "./pages/MapDetailPage";
import AppLayout from "./components/AppLayout";
import ErrorBoundary from "./components/ErrorBoundary";

const theme = createTheme({
  primaryColor: "blue",
  defaultRadius: "sm",
});

function App() {
  return (
    <MantineProvider theme={theme}>
      <Notifications position="top-right" />
      <ErrorBoundary>
        <Router>
          <AppLayout>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/maps/:id" element={<MapDetailPage />} />
            </Routes>
          </AppLayout>
        </Router>
      </ErrorBoundary>
    </MantineProvider>
  );
}

export default App;
