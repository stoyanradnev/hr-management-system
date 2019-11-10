package com.emerchantpay.hr.managementsystem.service;

import com.emerchantpay.hr.managementsystem.domain.Address;
import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.repository.EmployeeRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void updateEmployee_whenEmployeeExists_shouldUpdate() {

        Employee employee = createEmployee(1L, "firstName", "lastNameChanged", LocalDate.now(), BigDecimal.TEN);
        String id = "1";
        Employee existingEmployee = createEmployee(1L, "firstName", "lastName", LocalDate.now(), BigDecimal.TEN);
        when(employeeRepository.findById(Long.valueOf(id))).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);
        ArgumentCaptor<Employee> employeeArgumentCaptor = ArgumentCaptor.forClass(Employee.class);

        employeeService.updateOrCreateEmployee(employee, id);

        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(employeeArgumentCaptor.capture());
        assertEquals("lastNameChanged", employeeArgumentCaptor.getValue().getLastName());
    }

    @Test
    void updateEmployee_whenEmployeeDoesNotExists_shouldInsert() {

        Employee employee = createEmployee(1L, "firstName", "lastName", LocalDate.now(), BigDecimal.TEN);
        String id = "1";
        when(employeeRepository.findById(Long.valueOf(id))).thenReturn(Optional.empty());
        when(employeeRepository.save(eq(employee))).thenReturn(employee);

        Employee savedEmployee = employeeService.updateOrCreateEmployee(employee, id);

        verify(employeeRepository, times(1)).save(eq(employee));
        assertEquals("firstName", savedEmployee.getFirstName());
        assertEquals("lastName", savedEmployee.getLastName());
    }

    @Test
    void deleteEmployee_whenEmployeeExists_shouldDeleteAndReturn() {
        Employee employee = createEmployee();
        String id = employee.getId().toString();
        when(employeeRepository.findById(any())).thenReturn(Optional.of(employee));

        Optional<Employee> result = employeeService.deleteEmployee(id);

        verify(employeeRepository, times(1)).findById(eq(employee.getId()));
        verify(employeeRepository, times(1)).deleteById(eq(employee.getId()));
        assertTrue(result.isPresent());
    }

    @Test
    void deleteEmployee_whenEmployeeDoesNotExists_shouldReturnOptionalEmpty() {
        Employee employee = createEmployee();
        String id = employee.getId().toString();
        when(employeeRepository.findById(any())).thenReturn(Optional.empty());

        Optional<Employee> result = employeeService.deleteEmployee(id);

        verify(employeeRepository, times(1)).findById(eq(employee.getId()));
        verify(employeeRepository, never()).deleteById(any());
        assertTrue(result.isEmpty());
    }

    @Test
    void getEmployee_whenExists_shouldReturnEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(createEmployee()));

        Optional<Employee> employee = employeeService.getEmployee("1");

        verify(employeeRepository, times(1)).findById(eq(1L));
        assertTrue(employee.isPresent());
    }

    @Test
    void getEmployee_whenDoesNotExists_shouldReturnOptionalEmpty() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Employee> employee = employeeService.getEmployee("1");

        verify(employeeRepository, times(1)).findById(eq(1L));
        assertTrue(employee.isEmpty());
    }

    @Test
    void listEmployees_shouldReturnAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(createEmployeeWithId(1L), createEmployeeWithId(2L)));

        Collection<Employee> employees = employeeService.listEmployees();

        verify(employeeRepository, times(1)).findAll();
        assertEquals(2, employees.size());
        assertTrue(employees.stream().anyMatch(employee -> employee.getId().equals(1L)));
        assertTrue(employees.stream().anyMatch(employee -> employee.getId().equals(2L)));
    }

    @Test
    void deleteMultipleEmployees() {
        List<Long> employeesIds = List.of(1L, 2L, 3L);
        List<Employee> employeeWithIdFromDb = List.of(
                createEmployeeWithId(1L),
                createEmployeeWithId(3L)
        );
        when(employeeRepository.findAllById(employeesIds)).thenReturn(employeeWithIdFromDb);

        List<Long> deletedEmployeeIds = employeeService.deleteMultipleEmployees(employeesIds);

        verify(employeeRepository, times(1)).findAllById(eq(employeesIds));
        verify(employeeRepository, times(1)).deleteAll(eq(employeeWithIdFromDb));
        assertEquals(2, deletedEmployeeIds.size());
        assertTrue(deletedEmployeeIds.contains(1L));
        assertTrue(deletedEmployeeIds.contains(3L));
    }

    private Employee createEmployee(Long id, String firstName, String lastName, LocalDate startDate,
            BigDecimal salary) {

        return Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .startDate(startDate)
                .salary(salary)
                .telephoneNumber("555 252")
                .addresses(Collections.singletonList(Address.builder()
                        .country("Bulgaria")
                        .city("Sofia")
                        .postCode("1000")
                        .streetNumber("1")
                        .street("Street")
                        .build()))
                .build();
    }

    private Employee createEmployee() {
        return createEmployee(1L, "firstName", "lastName", LocalDate.now(), new BigDecimal("1"));
    }

    private Employee createEmployeeWithId(Long id) {
        return createEmployee(id, "firstName", "lastName", LocalDate.now(), new BigDecimal("555"));
    }
}