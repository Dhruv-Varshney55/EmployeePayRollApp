package com.example.EmployeePayRollApp.interfaces;

import com.example.EmployeePayRollApp.dto.EmployeeDTO;
import com.example.EmployeePayRollApp.entity.EmployeeEntity;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeInterface {
    public EmployeeDTO get(Long id) throws Exception;
    public EmployeeDTO create(EmployeeDTO newEmp) throws Exception;
    public EmployeeDTO edit(EmployeeDTO emp, Long id) throws Exception;
    public String delete(Long id);
}