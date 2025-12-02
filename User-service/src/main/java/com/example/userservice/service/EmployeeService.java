package com.example.userservice.service;

import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.model.Employee;
import com.example.userservice.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Employee> findByName(String name) {
        return employeeRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return employeeRepository.existsByName(name);
    }

    public Employee createEmployee(CreateEmployeeRequest req) {
        if (existsByName(req.name())) {
            throw new IllegalStateException("Employee name already taken");
        }

        Employee employee = new Employee();
        employee.setName(req.name());
        employee.setEmail(req.email());
        employee.setPassword(passwordEncoder.encode(req.password()));
        employee.setPhoneNumber(req.phoneNumber());
        employee.setAddress(req.address());
        employee.setProfilePictureUrl(req.profilePictureUrl());

        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, UpdateEmployeeRequest req) {
        Employee employee = getById(id);

        if (!employee.getName().equals(req.name())) {
            employeeRepository.findByName(req.name())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(id)) {
                            throw new IllegalStateException("Employee name already taken");
                        }
                    });
        }

        employee.setName(req.name());
        employee.setEmail(req.email());
        employee.setPhoneNumber(req.phoneNumber());
        employee.setAddress(req.address());
        employee.setProfilePictureUrl(req.profilePictureUrl());

        return employeeRepository.save(employee);
    }

    public Optional<Employee> authenticateByNameAndPassword(String name, String rawPassword) {
        return employeeRepository.findByName(name)
                .filter(e -> passwordEncoder.matches(rawPassword, e.getPassword()));
    }


    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee not found: " + id));
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
