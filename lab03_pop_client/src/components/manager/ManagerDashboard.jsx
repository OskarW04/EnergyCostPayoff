import { useState } from "react";
import Tasks from "./Tasks";
import Buildings from "./Buildings";
import Payments from "./Payments";
import LoutDelButtons from "../auth/LoutDelButtons"

function ManagerDashboard() {
    const [activeTab, setActiveTab] = useState("tasks");

    return (
        <div>
            <h1>Manager Dashboard</h1>
            <div>
                <button onClick={() => setActiveTab("tasks")}>Tasks</button>
                <button onClick={() => setActiveTab("buildings")}>Buildings</button>
                <button onClick={() => setActiveTab("payments")}>Payments</button>
            </div>
            <div>
                {activeTab === "tasks" && <Tasks />}
                {activeTab === "buildings" && <Buildings />}
                {activeTab === "payments" && <Payments />}
            </div>
            <LoutDelButtons/>
        </div>
    );
}

export default ManagerDashboard;
