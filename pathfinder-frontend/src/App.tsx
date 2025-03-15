import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import { MantineProvider } from "@mantine/core";
import HomePage from "./pages/HomePage";
import MapDetailPage from "./pages/MapDetailPage";

function App() {
  return (
    <MantineProvider>
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/maps/:id" element={<MapDetailPage />} />
        </Routes>
      </Router>
    </MantineProvider>
  );
}

export default App;
