package com.example.EmployeePayRollApp.controller;

import com.example.EmployeePayRollApp.dto.EmployeeDTO;
import com.example.EmployeePayRollApp.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employeepayrollservice")
public class EmployeeController {

    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // UC2 --> CRUD Operations
    @GetMapping("/get/{id}")
    public EmployeeDTO get(@PathVariable Long id){
        return employeeService.get(id);
    }

    @PostMapping("/create")
    public EmployeeDTO create(@RequestBody EmployeeDTO newEmp){
        return employeeService.create(newEmp);
    }

    @PutMapping("/edit/{id}")
    public EmployeeDTO edit(@RequestBody EmployeeDTO emp, @PathVariable Long id){
        return employeeService.edit(emp, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        return employeeService.delete(id);
    }

}