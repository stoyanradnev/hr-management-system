package com.emerchantpay.hr.managementsystem.service;

import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.repository.EmployeeRepository;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Collection<Employee> listEmployees(String sortBy, Integer page, Integer size) {
        Set<String> employeeFieldNames = Stream.of(Employee.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        PageRequest pageRequest = buildPageRequest(page, size);

        if (employeeFieldNames.contains(sortBy)) {
            PageRequest sortedPageRequest = PageRequest
                    .of(pageRequest.getPageNumber(), pageRequest.getPageSize(), Sort.by(sortBy));
            return employeeRepository.findAll(sortedPageRequest).getContent();
        }
        return employeeRepository.findAll(pageRequest).getContent();
    }

    private PageRequest buildPageRequest(Integer page, Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = 100;
        }
        return PageRequest.of(page, size);
    }
}
