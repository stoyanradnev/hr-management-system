package com.emerchantpay.hr.managementsystem.service;

import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.repository.EmployeeRepository;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee employee, String employeeId) {
        Long id = Long.parseLong(employeeId);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();
            //update
            existingEmployee.setFirstName(employee.getFirstName());
            existingEmployee.setLastName(employee.getLastName());
            existingEmployee.setAddresses(employee.getAddresses());
            existingEmployee.setSalary(employee.getSalary());
            existingEmployee.setStartDate(employee.getStartDate());
            existingEmployee.setTelephoneNumber(employee.getTelephoneNumber());
            employeeRepository.save(existingEmployee);
            return existingEmployee;
        }
        return employeeRepository.save(employee);
    }

    public Optional<Employee> deleteEmployee(String employeeId) {
        Long id = Long.parseLong(employeeId);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            employeeRepository.deleteById(id);
            return employeeOptional;
        }
        return Optional.empty();
    }

    public Optional<Employee> getEmployee(String employeeId) {
        Long id = Long.parseLong(employeeId);
        return employeeRepository.findById(id);
    }

    public Collection<Employee> listEmployees() {
        return employeeRepository.findAll();
    }
}
