package com.example.lab03_pop.repository;

import com.example.lab03_pop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByTenantIdAndPayedFalse(int tenantId);
    List<Payment> findByTenantIdAndPayedTrue(int id);
    @Query("SELECT p FROM Payment p WHERE p.property.building.manager.id = :managerId")
    List<Payment> findAllByManagerId(@Param("managerId") int managerId);
    @Query("SELECT p FROM Payment p WHERE p.property.building.id = :buildingId")
    List<Payment> findAllByBuildingId(@Param("buildingId") int buildingId);
}
