import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API } from "../../utils/Api";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleLogin = async () => {
        try {
            const response = await API.post("/api/user/login", null, {
                params: { username, password },
            });
            sessionStorage.setItem("token", response.data.token);
            sessionStorage.setItem("username", username);
            alert("Login successful!");
            window.location.reload();
        } catch (error) {
            console.error("Error during login:", error);
            alert(error);
        }
    };

    return (
        <div>
            <h1>Login</h1>
            <input
                type="text"
                placeholder="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <button onClick={handleLogin}>Login</button>
            <a href="/register">Register</a>
        </div>
    );
}

export default Login;