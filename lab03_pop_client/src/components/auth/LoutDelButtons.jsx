import { API } from "../../utils/Api";
import {logout} from "../../utils/Auth.js"
import { useNavigate } from "react-router-dom";
import { useState } from "react";

function LoutDelButtons(){
    let navigate = useNavigate();
    const [isDeleteOpen, setIsDeleteOpen] = useState(false);
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleDelete = async () => {
        try {
            await API.delete("/api/user/delete",{
                params: { username, password },
            });
            logout;
            alert("Account deleted");
            navigate("/register")
        } catch (error) {
            console.error("Error during deleting:", error);
            alert("Invalid credentials.");
        }
    }

    const openDelete = () => {
        setIsDeleteOpen(true);
    };
    
    const closeDelete = () => {
        setIsDeleteOpen(false);
    };

    return(
        <div className="logoutdelete">
            <button onClick={logout}>Logut</button>
            <button onClick={openDelete}>Delete account</button>

            {isDeleteOpen && (
            <div className="delWindow">
                <div>
                    <label>Log in to delete your account</label>
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
                    <button onClick={handleDelete}>Delete</button>
                    <a style={{ cursor: "pointer" }} onClick={closeDelete}>X</a>
                </div>
            </div>
            )}
        </div>
        
    );

}
export default LoutDelButtons;