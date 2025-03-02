import { useState, useEffect } from "react";
import { API } from "../../utils/Api";

function Buildings() {
    const [buildings, setBuildings] = useState([]);
    const [selectedBuilding, setSelectedBuilding] = useState(null);
    const [properties, setProperties] = useState([]);
    const [tenants, setTenants] = useState([]);
    const [selectedTenantUsername, setSelectedTenantUsername] = useState("");

    useEffect(() => {
        const fetchBuildings = async () => {
            try {
                const response = await API.get("/api/building/manager/get");
                setBuildings(response.data);
            } catch (error) {
                console.error("Error fetching buildings:", error.request.response);
                alert("Failed to fetch buildings.");
            }
        };

        fetchBuildings();
    }, []);

    useEffect(() => {
        const fetchTenants = async () => {
            try {
                const response = await API.get("/api/user/manager/getTenants");
                setTenants(response.data);
            } catch (error) {
                console.error("Error fetching tenants:", error.request.response);
                alert("Failed to fetch tenants.");
            }
        };

        fetchTenants();
    }, []);

    const addBuilding = async () => {
        try {
            const response = await API.post("/api/building/manager/add");
            setBuildings([...buildings, response.data]);
            alert("Building added successfully!");
        } catch (error) {
            console.error("Error adding building:", error.request.response);
            alert("Failed to add building.");
        }
    };

    const deleteBuilding = async (buildingId) => {
        try {
            await API.delete("/api/building/manager/delete", {
                params: { buildingId },
            });
            setBuildings(buildings.filter((building) => building.id !== buildingId));
            alert("Building deleted successfully!");
        } catch (error) {
            console.error("Error deleting building:", error.request.response);
            alert("Failed to delete building.");
        }
    };

    const fetchProperties = async (buildingId) => {
        try {
            const response = await API.get("/api/property/manager/get", {
                params: { buildingId },
            });
            setProperties(response.data);
            setSelectedBuilding(buildingId);
        } catch (error) {
            console.error("Error fetching properties:", error.request.response);
            alert("Failed to fetch properties.");
        }
    };

    const addProperty = async () => {
        if (!selectedBuilding || !selectedTenantUsername) {
            alert("Please select a building and a tenant.");
            return;
        }

        try {
            const response = await API.post("/api/property/manager/add", null, {
                params: {
                    buildingId: selectedBuilding,
                    tenantUsername: selectedTenantUsername,
                },
            });
            setProperties([...properties, response.data]);
            setSelectedTenantUsername("");
            alert("Property added successfully!");
        } catch (error) {
            console.error("Error adding property:", error.request.response);
            alert("Failed to add property.");
        }
    };

    const deleteProperty = async (propertyId) => {
        try {
            await API.delete("/api/property/manager/delete", {
                params: { propertyId },
            });
            setProperties(properties.filter((property) => property.id !== propertyId));
            alert("Property deleted successfully!");
        } catch (error) {
            console.error("Error deleting property:", error.request.response);
            alert("Failed to delete property.");
        }
    };

    return (
        <div>
            <h1>Buildings</h1>
            <button onClick={addBuilding}>Add Building</button>
            {buildings.map((building) => (
                <div key={building.id}>
                    <h3>Building ID: {building.id}</h3>
                    <p>Building Reading: {building.buildingReading}</p>
                    <p>Previous Building Reading: {building.previousBuildingReading}</p>
                    <button onClick={() => fetchProperties(building.id)}>View Properties</button>
                    <button onClick={() => deleteBuilding(building.id)}>Delete</button>
                </div>
            ))}

            {selectedBuilding && (
                <div>
                    <h2>Properties in Building {selectedBuilding}</h2>
                    {properties.length > 0 ? (
                        properties.map((property) => (
                            <div key={property.id}>
                                <p>Property ID: {property.id}</p>
                                <p>Tenant Username: {property.tenantDetails?.username || "Unassigned"}</p>
                                <p>Current Reading: {property.readingValue}</p>
                                <p>Previous Reading: {property.previousReadingValue}</p>
                                <button onClick={() => deleteProperty(property.id)}>Delete Property</button>
                            </div>
                        ))
                    ) : (
                        <p>No properties found in this building.</p>
                    )}
                    <div>
                        <h3>Add Property</h3>
                        <label>
                            Select Tenant:
                            <select
                                value={selectedTenantUsername}
                                onChange={(e) => setSelectedTenantUsername(e.target.value)}
                            >
                                <option value="">--Select a Tenant--</option>
                                {tenants.map((tenant) => (
                                    <option key={tenant.username} value={tenant.username}>
                                        {tenant.username}
                                    </option>
                                ))}
                            </select>
                        </label>
                        <button onClick={addProperty}>Add Property</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default Buildings;
