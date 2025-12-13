package com.example.userservice.controller;

import com.example.userservice.dto.Department.CreateDepartmentRequest;
import com.example.userservice.dto.Department.UpdateDepartmentRequest;
import com.example.userservice.model.Department;
import com.example.userservice.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getAll() {
        return departmentService.getAll();
    }

    @GetMapping("/{id}")
    public Department getById(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    @PostMapping
    public Department create(@RequestBody CreateDepartmentRequest req) {
        return departmentService.create(req.name());
    }

    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody UpdateDepartmentRequest req) {
        return departmentService.update(id, req.name());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}