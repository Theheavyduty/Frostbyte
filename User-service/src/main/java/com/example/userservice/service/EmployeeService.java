package com.example.userservice.service;

import com.example.userservice.dto.Employee.ChangePasswordRequest;
import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.EmployeeLoginRequest;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.model.Employee;
import com.example.userservice.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder,
                           FileStorageService fileStorageService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }


    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
    }

    public Employee create(CreateEmployeeRequest req) {
        Employee e = new Employee();
        e.setName(req.name());
        e.setEmail(req.email());
        e.setPhoneNumber(req.phoneNumber());
        e.setAddress(req.address());
        e.setProfilePictureUrl(null);

        if (req.password() == null || req.password().isBlank()) {
            throw new IllegalArgumentException("Password is required for employees");
        }
        e.setPassword(passwordEncoder.encode(req.password()));

        return employeeRepository.save(e);
    }

    public Employee update(Long id, UpdateEmployeeRequest req) {
        Employee e = getById(id);

        if (req.name() != null && !req.name().isBlank()) {
            e.setName(req.name());
        }
        if (req.email() != null && !req.email().isBlank()) {
            e.setEmail(req.email());
        }
        if (req.phoneNumber() != null) {
            e.setPhoneNumber(req.phoneNumber());
        }
        if (req.address() != null && !req.address().isBlank()) {
            e.setAddress(req.address());
        }

        // profilePictureUrl is managed by updateProfilePicture – ignore here
        return employeeRepository.save(e);
    }

    public void delete(Long id) {
        Employee e = getById(id);

        // Delete profile picture from disk if present
        fileStorageService.deleteProfilePicture(e.getProfilePictureUrl());

        employeeRepository.delete(e);
    }

    // === PROFILE PICTURE ====================================================

    public Employee updateProfilePicture(Long id, MultipartFile file) throws IOException {
        Employee e = getById(id);

        // Delete old picture (if any)
        fileStorageService.deleteProfilePicture(e.getProfilePictureUrl());

        // Store new picture under uploads/profile-pictures/employees/{id}/...
        String url = fileStorageService.storeEmployeeProfilePicture(id, file);
        e.setProfilePictureUrl(url);

        return employeeRepository.save(e);
    }

    // === AUTH / PASSWORD ====================================================

    public Employee login(EmployeeLoginRequest req) {
        Employee e = employeeRepository.findByName(req.name())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (!passwordEncoder.matches(req.password(), e.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return e;
    }

    public void changePassword(Long id, ChangePasswordRequest req) {
        Employee e = getById(id);

        if (!passwordEncoder.matches(req.oldPassword(), e.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        if (req.newPassword() == null || req.newPassword().isBlank()) {
            throw new IllegalArgumentException("New password cannot be empty");
        }

        e.setPassword(passwordEncoder.encode(req.newPassword()));
        employeeRepository.save(e);
    }

    // === COMPATIBILITY HELPERS USED BY CONTROLLERS =========================

    // Used by EmployeeController when checking for duplicate names
    public boolean existsByName(String name) {
        return employeeRepository.findByName(name).isPresent();
    }

    // Wrappers so older controller code like createEmployee(...) still works
    public Employee createEmployee(CreateEmployeeRequest req) {
        return create(req);
    }

    public Employee updateEmployee(Long id, UpdateEmployeeRequest req) {
        return update(id, req);
    }

    public List<Employee> getAllEmployees() {
        return getAll();
    }

    public void deleteEmployee(Long id) {
        delete(id);
    }

    public Optional<Employee> authenticateByNameAndPassword(String name, String password) {
        return employeeRepository.findByName(name)
                .filter(e -> passwordEncoder.matches(password, e.getPassword()));
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }
}
