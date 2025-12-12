package com.example.userservice.controller;

import com.example.userservice.dto.Employee.ChangePasswordRequest;
import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.EmployeeLoginRequest;
import com.example.userservice.dto.Employee.EmployeeResponse;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.model.Employee;
import com.example.userservice.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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

    @GetMapping
    public List<EmployeeResponse> getAll() {
        return employeeService.getAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable Long id) {
        return toResponse(employeeService.getById(id));
    }

    @PostMapping
    public EmployeeResponse create(@RequestBody CreateEmployeeRequest req) {
        return toResponse(employeeService.create(req));
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(
            @PathVariable Long id,
            @RequestBody UpdateEmployeeRequest req
    ) {
        return toResponse(employeeService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/profile-picture")
    public EmployeeResponse uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        return toResponse(employeeService.updateProfilePicture(id, file));
    }

    @PostMapping("/login")
    public EmployeeResponse login(@RequestBody EmployeeLoginRequest req) {
        return toResponse(employeeService.login(req));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest req
    ) {
        employeeService.changePassword(id, req);
        return ResponseEntity.ok().body(java.util.Map.of("status", "ok"));
    }
}
