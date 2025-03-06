package com.example.EmployeePayRollApp.repository;

import com.example.EmployeePayRollApp.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
}