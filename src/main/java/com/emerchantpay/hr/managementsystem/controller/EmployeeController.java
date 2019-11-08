package com.emerchantpay.hr.managementsystem.controller;

import com.emerchantpay.hr.managementsystem.domain.BulkDeleteEmployeesIds;
import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.service.EmployeeService;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {

        Employee createdEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String employeeId,
            @RequestBody Employee employee) {

        Employee createdOrUpdatedEmployee = employeeService.updateOrCreateEmployee(employee, employeeId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(createdOrUpdatedEmployee);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") String employeeId) {

        Optional<Employee> employee = employeeService.deleteEmployee(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") String employeeId) {
        Optional<Employee> employee = employeeService.getEmployee(employeeId);
        if (employee.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(employee.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<Collection<Employee>> listEmployees(
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "asc", required = false) Boolean ascending,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size) {
        if (sortBy == null && page == null && size == null && ascending == null) {
            Collection<Employee> employees = employeeService.listEmployees();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(employees);
        }
        Collection<Employee> employees = employeeService.listEmployees(sortBy, page, size, ascending);
        return ResponseEntity.status(HttpStatus.OK)
                .body(employees);
    }

    @PostMapping("/bulk-delete")
    public ResponseEntity<BulkDeleteEmployeesIds> deleteMultipleEmployees(
            @RequestBody BulkDeleteEmployeesIds bulkDeleteEmployeesIds) {
        List<Long> deletedEmployeesIds = employeeService
                .deleteMultipleEmployees(bulkDeleteEmployeesIds.getEmployeesIds());
        BulkDeleteEmployeesIds deletedIds = new BulkDeleteEmployeesIds(deletedEmployeesIds);
        return ResponseEntity.status(HttpStatus.OK)
                .body(deletedIds);
    }
}
