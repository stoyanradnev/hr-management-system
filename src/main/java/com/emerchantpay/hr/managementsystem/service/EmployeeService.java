package com.emerchantpay.hr.managementsystem.service;

import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.repository.EmployeeRepository;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateOrCreateEmployee(Employee employee, String employeeId) {
        Long id = Long.parseLong(employeeId);
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee existingEmployee = employeeOptional.get();
            //update
            copyEmployeeFields(employee, existingEmployee);
            employeeRepository.save(existingEmployee);
            return existingEmployee;
        }
        return employeeRepository.save(employee);
    }

    @Transactional
    private void copyEmployeeFields(Employee employee, Employee existingEmployee) {
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setAddresses(employee.getAddresses());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setStartDate(employee.getStartDate());
        existingEmployee.setTelephoneNumber(employee.getTelephoneNumber());
    }

    @Transactional
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

    public Collection<Employee> listEmployees(String sortBy, Integer page, Integer size, Boolean ascending) {
        //this could be extracted and calculated only once at startUp
        Set<String> employeeFieldNames = Stream.of(Employee.class.getDeclaredFields())
                .map(Field::getName)
                .collect(Collectors.toSet());

        PageRequest pageRequest = buildPageRequest(page, size);

        if (employeeFieldNames.contains(sortBy)) {
            Sort sortOrder = ascending ? Sort.by(sortBy) : Sort.by(sortBy).descending();
            PageRequest sortedPageRequest = PageRequest
                    .of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sortOrder);
            return employeeRepository.findAll(sortedPageRequest).getContent();
        }
        return employeeRepository.findAll(pageRequest).getContent();
    }

    @Transactional
    public List<Long> deleteMultipleEmployees(List<Long> employeeIds) {
        List<Employee> employees = employeeRepository.findAllById(employeeIds);
        if (employees.size() != employeeIds.size()) {
            log.warn("Not all employees have been found. Employees found [{}]", employees);
        }
        employeeRepository.deleteAll(employees);
        return employees.stream()
                .map(Employee::getId)
                .collect(Collectors.toList());
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
