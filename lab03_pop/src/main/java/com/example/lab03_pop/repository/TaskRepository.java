package com.example.lab03_pop.repository;

import com.example.lab03_pop.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByControllerIdAndCompletedFalse(int controllerId);

    List<Task> findAllByBuildingManagerIdAndCompletedTrue(int id);

    List<Task> findByBuildingId(int id);
}
