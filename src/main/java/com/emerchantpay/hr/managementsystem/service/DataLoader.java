package com.emerchantpay.hr.managementsystem.service;

import com.emerchantpay.hr.managementsystem.domain.Address;
import com.emerchantpay.hr.managementsystem.domain.Employee;
import com.emerchantpay.hr.managementsystem.repository.EmployeeRepository;
import com.sun.tools.javac.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DataLoader implements ApplicationRunner {

    private final EmployeeRepository employeeRepository;

    @Override
    public void run(ApplicationArguments args) {

        Employee employee = createEmployee("Stoyan", "Radnev", LocalDate.of(2019, 1, 1));
        Employee employee2 = createEmployee("Ivan", "Ivanov", LocalDate.of(2020, 1, 1));
        Employee employee3 = createEmployee("Georgi", "Georgiev", LocalDate.of(2016, 1, 1));

        List<Employee> employees = List.of(employee, employee2, employee3);
        employeeRepository.saveAll(employees);
        employees.forEach(e -> log.info("Employees [{}] saved.", e));
    }

    private Employee createEmployee(String firstName, String lastName, LocalDate startDate) {

        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .startDate(startDate)
                .salary(new BigDecimal("1000"))
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
}
