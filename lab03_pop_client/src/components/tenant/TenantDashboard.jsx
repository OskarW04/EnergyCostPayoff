import { useState } from "react";
import Properties from "./Properties";
import PaymentsTenant from "./PaymentsTenant";
import LoutDelButtons from "../auth/LoutDelButtons"

function TenantDashboard() {
    const [activeTab, setActiveTab] = useState("properties");

    return (
        <div>
            <h1>Tenant Dashboard</h1>
            <h2>Welcome, {sessionStorage.getItem("username")}</h2>
            <div>
                <button onClick={() => setActiveTab("properties")}>Properties</button>
                <button onClick={() => setActiveTab("payments")}>Payments</button>
            </div>
            <div>
                {activeTab === "properties" && <Properties />}
                {activeTab === "payments" && <PaymentsTenant />}
            </div>
            <LoutDelButtons/>
        </div>
    );
    
}

export default TenantDashboard;
