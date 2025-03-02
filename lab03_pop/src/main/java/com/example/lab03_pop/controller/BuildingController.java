package com.example.lab03_pop.controller;

import com.example.lab03_pop.entity.Building;
import com.example.lab03_pop.service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/building")
@RequiredArgsConstructor
public class BuildingController {
    private final BuildingService buildingService;

    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/manager/add")
    public ResponseEntity<Building> addBuilding(){
        return ResponseEntity.ok(buildingService.addBuilding());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/get")
    public ResponseEntity<List<Building>> getBuildings(){
        return ResponseEntity.ok(buildingService.getBuildings());
    }

    @PreAuthorize("hasRole('MANAGER')")
    @DeleteMapping("/manager/delete")
    public ResponseEntity<?> deleteBuilding(
            @RequestParam int buildingId
    ){
        return ResponseEntity.ok(buildingService.deleteBuilding(buildingId));
    }

    @PreAuthorize("hasRole('MANAGER')")
    @GetMapping("/manager/getAllId")
    public ResponseEntity<List<Integer>> getAllId(){
        return ResponseEntity.ok(buildingService.getAllBuildingsId());
    }
}
