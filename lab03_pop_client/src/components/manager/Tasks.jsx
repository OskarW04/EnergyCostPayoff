import { useState, useEffect } from "react";
import { API } from "../../utils/Api";
import { format } from "date-fns";

function Tasks() {
    const [tasks, setTasks] = useState([]);
    const [buildingId, setBuildingId] = useState("");
    const [controllerId, setControllerId] = useState("");
    const [scheduledDate, setScheduledDate] = useState("");
    const [isLoading, setIsLoading] = useState(true);
    const [buildings, setBuildings] = useState([]);
    const [controllers, setControllers] = useState([]);

    // Fetch tasks on mount
    useEffect(() => {
        const fetchTasksAndData = async () => {
            try {
                // Fetch tasks
                const tasksResponse = await API.get("/api/tasks/manager/get");
                setTasks(tasksResponse.data);

                // Fetch buildings
                const buildingsResponse = await API.get("/api/building/manager/getAllId");
                setBuildings(buildingsResponse.data);

                // Fetch controllers
                const controllersResponse = await API.get("/api/user/manager/getControllers");
                setControllers(controllersResponse.data);
            } catch (error) {
                console.error("Error fetching data:", error);
                alert("Failed to fetch data.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchTasksAndData();
    }, []);

    const createTask = async () => {
        if (!buildingId || !controllerId || !scheduledDate) {
            alert("Please provide all required fields.");
            return;
        }

        try {
            const response = await API.post("/api/tasks/manager/create", null, {
                params: {
                    bId: buildingId,
                    cId: controllerId,
                    sDate: format(new Date(scheduledDate), "dd-MM-yyyy"),
                },
            });
            setTasks([...tasks, response.data]);
            alert("Task created successfully!");
        } catch (error) {
            console.error("Error creating task:", error.request.response);
            alert("Failed to create task.");
        }
    };

    const deleteTask = async (taskId) => {
        try {
            await API.delete("/api/tasks/manager/delete", {
                params: { tId: taskId },
            });
            setTasks(tasks.filter((task) => task.id !== taskId));
            alert("Task deleted successfully!");
        } catch (error) {
            console.error("Error deleting task:", error.request.response);
            alert("Failed to delete task.");
        }
    };

    const processPayments = async (taskId, costPerUnit) => {
        try {
            await API.post("/api/billing/manager/create", null, {
                params: { tId: taskId, cost: costPerUnit },
            });

            setTasks((prevTasks) =>
                prevTasks.map((task) =>
                    task.id === taskId ? { ...task, paymentStatus: "Pending" } : task
                )
            );

            alert("Payments processed successfully!");
        } catch (error) {
            console.error("Error processing payments:", error.request.response);
            alert("Failed to process payments.");
        }
    };

    if (isLoading) return <p>Loading tasks...</p>;

    return (
        <div>
            <h2>Manager Tasks</h2>
            <div>
                <h3>Create a New Task</h3>
                <label>
                    Select Building:
                    <select
                        value={buildingId}
                        onChange={(e) => setBuildingId(e.target.value)}
                    >
                        <option value="">--Select a Building--</option>
                        {buildings.map((building) => (
                            <option key={building} value={building}>
                                ID: {building}
                            </option>
                        ))}
                    </select>
                </label>
                <label>
                    Select Controller:
                    <select
                        value={controllerId}
                        onChange={(e) => setControllerId(e.target.value)}
                    >
                        <option value="">--Select a Controller--</option>
                        {controllers.map((controller) => (
                            <option key={controller.id} value={controller.id}>
                                {controller.username}
                            </option>
                        ))}
                    </select>
                </label>
                <input
                    type="date"
                    placeholder="Scheduled Date"
                    value={scheduledDate}
                    onChange={(e) => {
                        setScheduledDate(e.target.value);
                    }}
                />
                <button onClick={createTask}>Create Task</button>
            </div>

            <div>
                <h3>Current Finished Tasks</h3>
                {tasks.length > 0 ? (
                    <ul>
                        {tasks.map((task) => (
                            <li key={task.id}>
                                <p>Task ID: {task.id}</p>
                                <p>Building ID: {task.building.id}</p>
                                <p>Controller Username: {task.controller.username}</p>
                                <p>Scheduled Date: {task.scheduledDate}</p>
                                <p>Completed: {task.completed ? "Yes" : "No"}</p>
                                {task.completed && (
                                    <div>
                                        <p>
                                            Payment Status:{" "}
                                            {task.paymentStatus === "PAID"
                                                ? "Zapłacone"
                                                : task.paymentStatus === "PENDING"
                                                ? "W przetwarzaniu"
                                                : "Niezapłacone"}
                                        </p>
                                        {task.paymentStatus === "UNPAID" && (
                                            <div>
                                                <input
                                                    type="number"
                                                    placeholder="Cost per Unit"
                                                    onChange={(e) =>
                                                        (task.costPerUnit = e.target.value)
                                                    }
                                                />
                                                <button
                                                    onClick={() =>
                                                        processPayments(task.id, task.costPerUnit)
                                                    }
                                                >
                                                    Process Payments
                                                </button>
                                            </div>
                                        )}
                                    </div>
                                )}
                                <button onClick={() => deleteTask(task.id)}>Delete Task</button>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No tasks available.</p>
                )}
            </div>
        </div>
    );
}

export default Tasks;
