package com.emerchantpay.hr.managementsystem.controller;

import com.emerchantpay.hr.managementsystem.domain.Employee;
import java.util.Collection;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("employees")
public class EmployeeController {

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String employeeId,
            @RequestBody Employee employee) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable("id") String employeeId) {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") String employeeId) {
        return null;
    }

    @GetMapping
    public ResponseEntity<Collection<Employee>> listEmployees() {
        return null;
    }
}
