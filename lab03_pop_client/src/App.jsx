import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import ManagerDashboard from "./components/manager/ManagerDashboard";
import ControllerDashboard from "./components/controller/ControllerDashboard";
import TenantDashboard from "./components/tenant/TenantDashboard";
import { isLoggedIn, getRole } from "./utils/Auth";

function App() {
    const role = isLoggedIn() ? getRole() : null;

    return (
        <Router>
            <Routes>
                <Route path="/" element={!isLoggedIn() ? <Login /> : role === "MANAGER" ? <ManagerDashboard /> : role === "CONTROLLER" ? <ControllerDashboard /> : <TenantDashboard />} />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
        
    );
}

export default App;