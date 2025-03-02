package com.example.lab03_pop.repository;

import com.example.lab03_pop.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
    List<Property> findAllByBuildingId(int buildingId);

    List<Property> findAllByTenantId(int id);
}
