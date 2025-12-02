package com.example.userservice.controller;

import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.EmployeeResponse;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.model.Employee;
import com.example.userservice.service.EmployeeService;
import com.example.userservice.service.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.userservice.dto.Employee.EmployeeLoginRequest;


import java.io.IOException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final FileStorageService fileStorageService;

    public EmployeeController(EmployeeService employeeService,
                              FileStorageService fileStorageService) {
        this.employeeService = employeeService;
        this.fileStorageService = fileStorageService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest req
    ) {
        if (employeeService.existsByName(req.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Employee employee = employeeService.createEmployee(req);
        EmployeeResponse resp = toResponse(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEmployeeRequest req
    ) {
        Employee updated = employeeService.updateEmployee(id, req);
        EmployeeResponse resp = toResponse(updated);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "/{id}/profile-picture", consumes = "multipart/form-data")
    public ResponseEntity<EmployeeResponse> uploadEmployeeProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        Employee employee = employeeService.getById(id);

        String url = fileStorageService.storeEmployeeProfilePicture(id, file);
        employee.setProfilePictureUrl(url);
        employeeService.save(employee);

        EmployeeResponse resp = toResponse(employee);
        return ResponseEntity.ok(resp);
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EmployeeResponse> loginEmployee(
            @Valid @RequestBody EmployeeLoginRequest req
    ) {
        return employeeService.authenticateByNameAndPassword(req.name(), req.password())
                .map(employee -> ResponseEntity.ok(toResponse(employee)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    private EmployeeResponse toResponse(Employee e) {
        return new EmployeeResponse(
                e.getId(),
                e.getName(),
                e.getEmail(),
                e.getPhoneNumber(),
                e.getAddress(),
                e.getProfilePictureUrl()
        );
    }
}
