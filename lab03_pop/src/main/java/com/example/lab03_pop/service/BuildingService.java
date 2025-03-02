package com.example.lab03_pop.service;

import com.example.lab03_pop.auth.CustomUserDetails;
import com.example.lab03_pop.entity.Building;
import com.example.lab03_pop.entity.Manager;
import com.example.lab03_pop.entity.Role;
import com.example.lab03_pop.entity.User;
import com.example.lab03_pop.repository.BuildingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;

    @Transactional
    public Building addBuilding(){
        CustomUserDetails currentUserDetails = getCurrentUser();

        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can add building");
        }

        Manager manager = (Manager) currentUser;

        Building building = new Building();
        building.setManager(manager);
        building.setBuildingReading(0.0);
        building.setPreviousBuildingReading(0.0);
        building.setProperties(new ArrayList<>());

        return buildingRepository.save(building);
    }

    @Transactional
    public List<Building> getBuildings(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only manager can view buildings");
        }

        Manager manager = (Manager) currentUser;
        return buildingRepository.findByManagerId(manager.getId());
    }

    @Transactional
    public String deleteBuilding(int buildingId){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can delete building");
        }
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building dont exist"));
        buildingRepository.delete(building);
        return "Building deleted";
    }

    public List<Integer> getAllBuildingsId(){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only manager can do this");
        }
        Manager manager = (Manager) currentUser;
        return buildingRepository.findByManagerId(manager.getId()).stream()
                .map(Building::getId)
                .toList();
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
