package com.example.userservice.controller;

import com.example.userservice.dto.Employee.EmployeeWithPasswordResponse;
import com.example.userservice.model.Employee;
import com.example.userservice.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
public class EmployeeAdminController {

    private final EmployeeService employeeService;

    public EmployeeAdminController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private EmployeeWithPasswordResponse toResponse(Employee e) {
        return new EmployeeWithPasswordResponse(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getPhoneNumber(),
                e.getAddress(),
                e.getProfilePictureUrl(),
                e.getPassword()
        );
    }

    @GetMapping
    public List<EmployeeWithPasswordResponse> getAllWithPassword() {
        return employeeService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EmployeeWithPasswordResponse getByIdWithPassword(@PathVariable Long id) {
        return toResponse(employeeService.getById(id));
    }
}
