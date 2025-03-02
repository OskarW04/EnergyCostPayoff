package com.example.lab03_pop.service;

import com.example.lab03_pop.auth.CustomUserDetails;
import com.example.lab03_pop.entity.*;
import com.example.lab03_pop.repository.BuildingRepository;
import com.example.lab03_pop.repository.PropertyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {
    private final BuildingRepository buildingRepository;
    private final PropertyRepository propertyRepository;
    private final UserService userService;

    @Transactional
    public Property addProperty(int buildingId, String tenantUsername){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();
        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can add properties to building");
        }

        Manager manager = (Manager) currentUser;

        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building does not exist"));

        if (building.getManager().getId() != manager.getId()) {
            throw new IllegalArgumentException("You do not manage this building");
        }

        User tenantUser = userService.findByUsername(tenantUsername);
        if (!tenantUser.getRole().equals(Role.TENANT)) {
            throw new IllegalArgumentException("The user is not a tenant");
        }
        Tenant tenant = (Tenant) tenantUser;

        Property property = new Property();
        property.setBuilding(building);
        property.setTenant(tenant);
        property.setReadingValue(0.0);
        property.setPreviousReadingValue(0.0);

        return propertyRepository.save(property);
    }

    @Transactional
    public List<Property> getPropertiesOfBuildingForManager(int buildingId) {
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.MANAGER)) {
            throw new IllegalArgumentException("Only managers can view properties of buildings");
        }

        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new IllegalArgumentException("Building does not exist"));

        if (building.getManager().getId() != currentUser.getId()) {
            throw new IllegalStateException("Building does not belong to this manager");
        }

        return propertyRepository.findAllByBuildingId(buildingId);
    }

    @Transactional
    public Map<Integer, List<Map<String, Object>>> getTenantProperties() {
        CustomUserDetails currentUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = currentUserDetails.user();

        if (!currentUser.getRole().equals(Role.TENANT)) {
            throw new IllegalArgumentException("Only tenants can access their properties.");
        }

        List<Property> properties = propertyRepository.findAllByTenantId(currentUser.getId());

        return properties.stream()
                .collect(Collectors.groupingBy(
                        property -> property.getBuilding().getId(),
                        Collectors.mapping(property -> {
                            Map<String, Object> propertyDetails = new HashMap<>();
                            propertyDetails.put("id", property.getId());
                            propertyDetails.put("readingValue", property.getReadingValue());
                            propertyDetails.put("previousReadingValue", property.getPreviousReadingValue());
                            return propertyDetails;
                        }, Collectors.toList())
                ));
    }
    @Transactional
    public String deleteProperty(int propertyId){
        CustomUserDetails currentUserDetails = getCurrentUser();
        User currentUser = currentUserDetails.user();

        if(!currentUser.getRole().equals(Role.MANAGER)){
            throw new IllegalArgumentException("Only manager can delete property");
        }
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property dont exist"));
        propertyRepository.delete(property);
        return "Property deleted";
    }

    private CustomUserDetails getCurrentUser() {
        return (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
