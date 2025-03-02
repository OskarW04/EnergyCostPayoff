package com.example.lab03_pop.repository;

import com.example.lab03_pop.entity.Controller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControllerRepository extends JpaRepository<Controller, Integer> {
}
