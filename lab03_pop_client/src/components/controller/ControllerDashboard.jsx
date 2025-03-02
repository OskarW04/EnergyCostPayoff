import TasksController from "./TaskController";
import LoutDelButtons from "../auth/LoutDelButtons";

function ControllerDashboard() {
    return (
        <div>
            <h1>Controller Dashboard</h1>
            <TasksController />
            <LoutDelButtons/>
        </div>
        
    );
}

export default ControllerDashboard;
