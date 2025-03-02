package com.example.lab03_pop.controller;

import com.example.lab03_pop.entity.Property;
import com.example.lab03_pop.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping("/manager/add")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Property> addProperty(
            @RequestParam int buildingId,
            @RequestParam String tenantUsername){
        return ResponseEntity.ok(propertyService.addProperty(buildingId, tenantUsername));
    }

    @GetMapping("/manager/get")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<Property>> getPropertiesOfBuildingForManager(
            @RequestParam int buildingId){
        return ResponseEntity.ok(propertyService.getPropertiesOfBuildingForManager(buildingId));
    }

    @GetMapping("/tenant/get")
    @PreAuthorize("hasRole('TENANT')")
    public ResponseEntity<Map<Integer, List<Map<String, Object>>>> getTenantProperties() {
        return ResponseEntity.ok(propertyService.getTenantProperties());
    }

    @DeleteMapping("/manager/delete")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> deleteProperty(
            @RequestParam int propertyId){
        return ResponseEntity.ok(propertyService.deleteProperty(propertyId));
    }


}
