package com.example.lab03_pop.service;

import com.example.lab03_pop.auth.CustomUserDetails;
import com.example.lab03_pop.entity.*;
import com.example.lab03_pop.repository.BuildingRepository;
import com.example.lab03_pop.repository.ControllerRepository;
import com.example.lab03_pop.repository.PropertyRepository;
import com.example.lab03_pop.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final BuildingRepository buildingRepository;
    private final PropertyRepository propertyRepository;
    private final ControllerRepository controllerRepository;

    @Transactional
    public Task createTask(int buildingId, int controllerId, LocalDate scheduledDate){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();
        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only managers can access this");
        }
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building not found"));

        if(building.getManager().getId() != currentUser.getId()){
            throw new IllegalArgumentException("This building does not belong to current manager");
        }

        Controller controller = controllerRepository.findById(controllerId)
                .orElseThrow(() -> new IllegalArgumentException("This controller dont exist"));

        Task task = new Task();
        task.setBuilding(building);
        task.setController(controller);
        task.setScheduledDate(scheduledDate);
        task.setCompleted(false);

        return taskRepository.save(task);
    }

    public List<Task> getTasksForController(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.CONTROLLER)){
            throw new IllegalArgumentException("Only controller can access this");
        }

        return taskRepository.findAllByControllerIdAndCompletedFalse(currentUser.getId());
    }

    public List<Task> getCompletedTasksForManager() {
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only managers can access this");
        }

        return taskRepository.findAllByBuildingManagerIdAndCompletedTrue(currentUser.getId());
    }

    @Transactional
    public Task completeTask(int taskId, Double generalReading, List<Double> propertyReadings, LocalDate actualDate){

        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.CONTROLLER)) {
            throw new IllegalArgumentException("Only controllers can complete tasks");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));

        if(task.getController().getId() != currentUser.getId()){
            throw new IllegalArgumentException("That's not this controller task");
        }

        Building building = task.getBuilding();
        if(building == null){
            throw new IllegalStateException("Task does not have associated building");
        }

        List<Property> properties = building.getProperties();
        if(properties == null || properties.size() != propertyReadings.size()){
            throw new IllegalArgumentException("Property readings size does not match size of properties in the building");

        }



        for(int i = 0; i < properties.size(); i++){
            Property property = properties.get(i);
            Double propertyReading = propertyReadings.get(i);
            if(propertyReading < property.getPreviousReadingValue()){
                throw new IllegalArgumentException("Reading value can't be smaller than previous");
            }
            property.setReadingValue(propertyReading);
            propertyRepository.save(property);
        }
        if(generalReading < building.getPreviousBuildingReading()){
            throw new IllegalArgumentException("Reading value can't be smaller than previous");
        }
        building.setBuildingReading(generalReading);
        buildingRepository.save(building);

        task.setActualDate(actualDate);
        task.setCompleted(true);
        return taskRepository.save(task);
    }

    @Transactional
    public String deleteTask(int taskId){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can do this");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found"));
        taskRepository.delete(task);
        return "Task deleted";
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
