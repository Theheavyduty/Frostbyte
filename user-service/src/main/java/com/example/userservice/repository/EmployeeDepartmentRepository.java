package com.example.userservice.repository;

import com.example.userservice.model.EmployeeDepartment;
import com.example.userservice.model.EmployeeDepartmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDepartmentRepository extends JpaRepository<EmployeeDepartment, EmployeeDepartmentId> {

    @Query("SELECT ed FROM EmployeeDepartment ed JOIN FETCH ed.department WHERE ed.employee.id = :employeeId")
    List<EmployeeDepartment> findByEmployeeIdWithDepartment(@Param("employeeId") Long employeeId);

    void deleteByEmployeeId(Long employeeId);
}