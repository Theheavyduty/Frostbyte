package com.example.userservice.controller;

import com.example.userservice.dto.Children.ChildrenResponse;
import com.example.userservice.dto.Employee.ChangePasswordRequest;
import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.EmployeeResponse;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.service.ChildrenService;
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
    private final ChildrenService childrenService;

    public EmployeeController(EmployeeService employeeService, ChildrenService childrenService) {
        this.employeeService = employeeService;
        this.childrenService = childrenService;
    }

    @GetMapping
    public List<EmployeeResponse> getAll() {
        return employeeService.getAllWithDepartments();
    }

    @GetMapping("/{id}")
    public EmployeeResponse getById(@PathVariable Long id) {
        return employeeService.getByIdWithDepartments(id);
    }

    @PostMapping
    public EmployeeResponse create(@RequestBody CreateEmployeeRequest req) {
        return employeeService.create(req);
    }

    @PutMapping("/{id}")
    public EmployeeResponse update(@PathVariable Long id, @RequestBody UpdateEmployeeRequest req) {
        return employeeService.update(id, req);
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
        return employeeService.updateProfilePicture(id, file);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest req
    ) {
        employeeService.changePassword(id, req);
        return ResponseEntity.ok().body(java.util.Map.of("status", "ok"));
    }


    @GetMapping("/departments/{departmentId}/children")
    public List<ChildrenResponse> getChildrenByDepartment(@PathVariable Long departmentId) {
        return childrenService.getChildrenByDepartmentId(departmentId);
    }

    @GetMapping("/departments/{departmentName}/children/by-name")
    public List<ChildrenResponse> getChildrenByDepartmentName(@PathVariable String departmentName) {
        return childrenService.getChildrenByDepartmentName(departmentName);
    }
}