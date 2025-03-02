import { useState } from "react";
import { API } from "../../utils/Api";
import { useNavigate } from "react-router";

function Register() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("TENANT");
    let navigate = useNavigate();
    
    const handleRegister = async () => {
        try {
            await API.post("/api/user/register", null, {
                params: { username, password, role },
            });
            alert("Registration successful. Please login");
            navigate("/")
        } catch (error) {
            alert(error.request.response);
        }
    };

    return (
        <div>
            <h1>Register</h1>
            <input type="text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
            <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
            <select value={role} onChange={(e) => setRole(e.target.value)}>
                <option value="TENANT">Tenant</option>
                <option value="MANAGER">Manager</option>
                <option value="CONTROLLER">Controller</option>
            </select>
            <button onClick={handleRegister}>Register</button>
            <a href="/">Login</a>
        </div>
    );
}

export default Register;