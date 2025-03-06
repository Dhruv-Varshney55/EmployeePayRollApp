package com.example.EmployeePayRollApp.service;

import com.example.EmployeePayRollApp.dto.EmployeeDTO;
import com.example.EmployeePayRollApp.entity.EmployeeEntity;
import com.example.EmployeePayRollApp.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public EmployeeDTO get(Long id){
        EmployeeEntity empFound = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find Employee with given id"));
        EmployeeDTO empDto = new EmployeeDTO(empFound.getName(), empFound.getSalary());
        empDto.setId(empFound.getId());
        return empDto;
    }

    public EmployeeDTO create(EmployeeDTO newEmp){
        EmployeeEntity newEntity = new EmployeeEntity(newEmp.getName(), newEmp.getSalary());
        employeeRepository.save(newEntity);
        EmployeeDTO emp = new EmployeeDTO(newEntity.getName(), newEntity.getSalary());
        emp.setId(newEntity.getId());
        return emp;
    }

    public EmployeeDTO edit(EmployeeDTO emp, Long id){
        // Finding Employee
        EmployeeEntity foundEmp = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("No employee found for given id"));

        // Updating Details
        foundEmp.setName(emp.getName());
        foundEmp.setSalary(emp.getSalary());

        // Saving in Database
        employeeRepository.save(foundEmp);

        // Creating DTO to Return
        EmployeeDTO employeeDTO = new EmployeeDTO(foundEmp.getName(), foundEmp.getSalary());
        employeeDTO.setId(foundEmp.getId());

        return employeeDTO;
    }

    public String delete(Long id){
        EmployeeEntity foundEmp = employeeRepository.findById(id).orElseThrow(()->new RuntimeException("No employee found for given id"));
        employeeRepository.delete(foundEmp);
        return "Employee Deleted";
    }
}