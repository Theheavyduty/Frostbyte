package com.example.userservice.service;

import com.example.userservice.model.Department;
import com.example.userservice.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    public Department getById(Long id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found: " + id));
    }


    public Department create(String name) {
        if (departmentRepository.existsByName(name)) {
            throw new IllegalArgumentException("Department already exists: " + name);
        }

        Department department = Department.builder()
                .name(name)
                .build();

        return departmentRepository.save(department);
    }

    public Department update(Long id, String name) {
        Department department = getById(id);

        if (name != null && !name.isBlank()) {
            departmentRepository.findByName(name).ifPresent(existing -> {
                if (!existing.getId().equals(id)) {
                    throw new IllegalArgumentException("Department name already exists: " + name);
                }
            });
            department.setName(name);
        }

        return departmentRepository.save(department);
    }

    public void delete(Long id) {
        Department department = getById(id);
        departmentRepository.delete(department);
    }


}