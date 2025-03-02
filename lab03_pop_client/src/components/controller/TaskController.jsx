import { useState, useEffect } from "react";
import { API } from "../../utils/Api";
import { format } from "date-fns";

function TasksController() {
    const [tasks, setTasks] = useState([]);
    const [completedTask, setCompletedTask] = useState({
        taskId: "",
        generalReading: "",
        propertyReadings: [],
        actualDate: "",
    });

    useEffect(() => {
        const fetchTasks = async () => {
            try {
                const response = await API.get("/api/tasks/controller/get");
                setTasks(response.data);
            } catch (error) {
                console.error("Error fetching tasks:", error.request.response);
                alert("Failed to fetch tasks.");
            }
        };

        fetchTasks();
    }, []);

    const handleCompleteTask = async () => {
        try {
            const params = {
                tId: completedTask.taskId,
                gRd: parseFloat(completedTask.generalReading),
                pRds: completedTask.propertyReadings.join(","), 
                fDate: completedTask.actualDate,
            };

            await API.put("/api/tasks/controller/complete", null, { params }); 
            alert("Task completed successfully!");
            setTasks(tasks.filter((task) => task.id !== completedTask.taskId));
            setCompletedTask({
                taskId: "",
                generalReading: "",
                propertyReadings: [],
                actualDate: "",
            });
        } catch (error) {
            console.error("Error completing task:", error.request.response);
            alert("Failed to complete the task.");
        }
    };

    const handleReadingChange = (index, value) => {
        const newReadings = [...completedTask.propertyReadings];
        newReadings[index] = value;
        setCompletedTask({ ...completedTask, propertyReadings: newReadings });
    };

    return (
        <div>
            <h1>Tasks for Controller</h1>
            {tasks.length === 0 ? (
                <p>No tasks available.</p>
            ) : (
                tasks.map((task) => (
                    <div key={task.id} style={{ border: "1px solid #ddd", padding: "10px", marginBottom: "10px" }}>
                        <p>Task ID: {task.id}</p>
                        <p>Building ID: {task.building.id}</p>
                        <p>Scheduled Date: {task.scheduledDate}</p>
                        <p>Completed: {task.completed ? "Yes" : "No"}</p>
                        <button
                            onClick={() =>
                                setCompletedTask({
                                    taskId: task.id,
                                    generalReading: "",
                                    propertyReadings: Array(task.building.properties.length).fill(""),
                                    actualDate: "",
                                })
                            }
                        >
                            Complete Task
                        </button>
                    </div>
                ))
            )}

            {completedTask.taskId && (
                <div>
                    <h2>Complete Task ID: {completedTask.taskId}</h2>
                    <div>
                        <label>General Reading:</label>
                        <input
                            type="number"
                            value={completedTask.generalReading}
                            onChange={(e) =>
                                setCompletedTask({ ...completedTask, generalReading: e.target.value })
                            }
                        />
                    </div>
                    <div>
                        <label>Property Readings:</label>
                        {completedTask.propertyReadings.map((reading, index) => (
                            <div key={index}>
                                <label>Property {index + 1}:</label>
                                <input
                                    type="number"
                                    value={reading}
                                    onChange={(e) => handleReadingChange(index, e.target.value)}
                                />
                            </div>
                        ))}
                    </div>
                    <div>
                        <label>Actual Date:</label>
                        <input
                            type="date"
                            value={completedTask.actualDate}
                            onChange={(e) =>
                                setCompletedTask({ ...completedTask, actualDate: e.target.value })
                            }
                        />
                    </div>
                    <button onClick={handleCompleteTask}>Submit</button>
                </div>
            )}
        </div>
    );
}

export default TasksController;
