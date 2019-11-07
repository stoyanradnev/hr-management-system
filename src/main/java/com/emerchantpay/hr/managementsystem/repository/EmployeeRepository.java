package com.emerchantpay.hr.managementsystem.repository;

import com.emerchantpay.hr.managementsystem.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
