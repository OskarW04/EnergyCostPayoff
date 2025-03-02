package com.example.lab03_pop.controller;

import com.example.lab03_pop.entity.Task;
import com.example.lab03_pop.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasRole('MAANGER')")
    @PostMapping("/manager/create")
    public ResponseEntity<Task> createTask(
            @RequestParam int bId,
            @RequestParam int cId,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy")LocalDate sDate){
        return ResponseEntity.ok(taskService.createTask(bId, cId, sDate));
    }

    @PreAuthorize("hasRole('CONTROLLER')")
    @GetMapping("/controller/get")
    public ResponseEntity<List<Task>> getTasks(){
        return ResponseEntity.ok(taskService.getTasksForController());
    }

    @PreAuthorize("hasRole('CONTROLLER')")
    @PutMapping("/controller/complete")
    public ResponseEntity<Task> completeTask(
            @RequestParam int tId,
            @RequestParam Double gRd,
            @RequestParam List<Double> pRds,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate fDate){
        return ResponseEntity.ok(taskService.completeTask(tId, gRd, pRds, fDate));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/get")
    public ResponseEntity<List<Task>> getCompletedTasksForManager(){
        return ResponseEntity.ok(taskService.getCompletedTasksForManager());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/manager/delete")
    public ResponseEntity<String> deleteTask(
            @RequestParam int tId){
        return ResponseEntity.ok(taskService.deleteTask(tId));
    }
}
