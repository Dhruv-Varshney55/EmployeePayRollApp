package com.example.EmployeePayRollApp.service;

import com.example.EmployeePayRollApp.dto.EmployeeDTO;
import com.example.EmployeePayRollApp.entity.EmployeeEntity;
import com.example.EmployeePayRollApp.interfaces.EmployeeInterface;
import com.example.EmployeePayRollApp.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmployeeService implements EmployeeInterface {
    @Autowired
    EmployeeRepository employeeRepository;

    ObjectMapper obj = new ObjectMapper();

    public EmployeeDTO get(Long id) throws Exception{

        EmployeeEntity empFound = employeeRepository.findById(id).orElseThrow(()-> {
            log.error("Cannot find employee with id {}", id);
            return new RuntimeException("Cannot find employee with given id");
        });

        EmployeeDTO empDto = new EmployeeDTO(empFound.getName(), empFound.getSalary());
        empDto.setId(empFound.getId());

        log.info("Employee DTO send for id: {} is : {}", id, obj.writeValueAsString(empDto));

        return empDto;
    }

    public EmployeeDTO create(EmployeeDTO newEmp) throws Exception{
        EmployeeEntity newEntity = new EmployeeEntity(newEmp.getName(), newEmp.getSalary());
        employeeRepository.save(newEntity);
        log.info("Employee saved in db: {}", obj.writeValueAsString(newEntity));
        EmployeeDTO emp = new EmployeeDTO(newEntity.getName(), newEntity.getSalary());
        emp.setId(newEntity.getId());
        log.info("Employee DTO sent: {}", obj.writeValueAsString(emp));
        return emp;
    }

    public EmployeeDTO edit(EmployeeDTO emp, Long id)throws Exception{
        // Find
        EmployeeEntity foundEmp =  employeeRepository.findById(id).orElseThrow(()-> {
            log.error("Cannot find employee with id : {}", id);
            return new RuntimeException("Cannot find employee with given id");
        });

        // Update
        foundEmp.setName(emp.getName());
        foundEmp.setSalary(emp.getSalary());

        // Save
        employeeRepository.save(foundEmp);
        log.info("Employee saved after editing in db is : {}", obj.writeValueAsString(foundEmp));

        // Create DTO
        EmployeeDTO employeeDTO = new EmployeeDTO(foundEmp.getName(), foundEmp.getSalary());
        employeeDTO.setId(foundEmp.getId());

        return employeeDTO;
    }

    public String delete(Long id){
        EmployeeEntity foundEmp = employeeRepository.findById(id).orElseThrow(()-> {
            log.error("Cannot find user with id : {}", id);
            return new RuntimeException("Cannot find user with given id");
        });
        employeeRepository.delete(foundEmp);

        return "Employee Deleted";
    }
}