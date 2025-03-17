package com.example.EmployeePayRollApp.controller;

import com.example.EmployeePayRollApp.dto.EmployeeDTO;
import com.example.EmployeePayRollApp.interfaces.EmployeeInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
@Slf4j
public class EmployeeController {

    ObjectMapper obj = new ObjectMapper();

    @Autowired
    EmployeeInterface employeeInterface;

    // UC2 CRUD Operations
    @GetMapping("/get/{id}")
    public EmployeeDTO get(@PathVariable Long id) throws Exception{
        log.info("Get: {}", id);
        return employeeInterface.get(id);
    }

    @PostMapping("/create")
    public EmployeeDTO create(@RequestBody EmployeeDTO newEmp) throws Exception{
        log.info("Create: {}", obj.writeValueAsString(newEmp));
        return employeeInterface.create(newEmp);
    }

    @PutMapping("/edit/{id}")
    public EmployeeDTO edit(@RequestBody EmployeeDTO emp, @PathVariable Long id) throws Exception{
        log.info("Edit: {} and body: {}", id, obj.writeValueAsString(emp));
        return employeeInterface.edit(emp, id);
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        log.info("Delete: {}", id);
        return employeeInterface.delete(id);
    }
}