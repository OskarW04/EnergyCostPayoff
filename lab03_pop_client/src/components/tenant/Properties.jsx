import { useState, useEffect } from "react";
import { API } from "../../utils/Api";

function Properties() {
    const [properties, setProperties] = useState([]);

    useEffect(() => {
        const fetchProperties = async () => {
            try {
                const response = await API.get("/api/property/tenant/get");
                setProperties(response.data);
            } catch (error) {
                console.error("Error fetching properties:", error.request.response);
                alert("Failed to fetch properties.");
            }
        };

        fetchProperties();
    }, []);

    return (
        <div>
            <h1>Your Properties</h1>
            {Object.keys(properties).length === 0 ? (
                <p>No properties found.</p>
            ) : (
                Object.entries(properties).map(([buildingId, propertyList]) => (
                    <div key={buildingId} style={{ border: "1px solid #ddd", padding: "10px", marginBottom: "10px" }}>
                        <h2>Building ID: {buildingId}</h2>
                        {propertyList.map((property, index) => (
                            <div key={index} style={{ marginBottom: "10px" }}>
                                <p>Property ID: {property.id}</p>
                                <p>Recent reading: {property.previousReadingValue}</p>
                            </div>
                        ))}
                    </div>
                ))
            )}
        </div>
    );
}

export default Properties;
