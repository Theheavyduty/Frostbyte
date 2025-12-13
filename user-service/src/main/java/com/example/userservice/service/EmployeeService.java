package com.example.userservice.service;

import com.example.userservice.dto.Employee.ChangePasswordRequest;
import com.example.userservice.dto.Employee.CreateEmployeeRequest;
import com.example.userservice.dto.Employee.EmployeeResponse;
import com.example.userservice.dto.Employee.UpdateEmployeeRequest;
import com.example.userservice.model.Department;
import com.example.userservice.model.Employee;
import com.example.userservice.model.EmployeeDepartment;
import com.example.userservice.repository.DepartmentRepository;
import com.example.userservice.repository.EmployeeDepartmentRepository;
import com.example.userservice.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeDepartmentRepository employeeDepartmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           EmployeeDepartmentRepository employeeDepartmentRepository,
                           PasswordEncoder passwordEncoder,
                           FileStorageService fileStorageService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.employeeDepartmentRepository = employeeDepartmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorageService = fileStorageService;
    }


    private EmployeeResponse toResponse(Employee employee) {
        List<EmployeeDepartment> assignments = employeeDepartmentRepository.findByEmployeeIdWithDepartment(employee.getId());

        List<EmployeeResponse.DepartmentSummary> departmentSummaries = assignments.stream()
                .map(ed -> new EmployeeResponse.DepartmentSummary(
                        ed.getDepartment().getId(),
                        ed.getDepartment().getName()
                ))
                .toList();

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhoneNumber(),
                employee.getAddress(),
                employee.getProfilePictureUrl(),
                departmentSummaries
        );
    }


    public List<EmployeeResponse> getAllWithDepartments() {
        return employeeRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public EmployeeResponse getByIdWithDepartments(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
        return toResponse(employee);
    }

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee getById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + id));
    }

    public EmployeeResponse create(CreateEmployeeRequest req) {
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

        Employee savedEmployee = employeeRepository.save(e);

        // Assign departments if provided
        if (req.departmentIds() != null && !req.departmentIds().isEmpty()) {
            List<Department> departments = departmentRepository.findAllById(req.departmentIds());
            for (Department department : departments) {
                EmployeeDepartment assignment = EmployeeDepartment.builder()
                        .employee(savedEmployee)
                        .department(department)
                        .build();
                employeeDepartmentRepository.save(assignment);
            }
        }

        return toResponse(savedEmployee);
    }

    public EmployeeResponse update(Long id, UpdateEmployeeRequest req) {
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

        if (req.departmentIds() != null) {
            employeeDepartmentRepository.deleteByEmployeeId(id);

            if (!req.departmentIds().isEmpty()) {
                List<Department> departments = departmentRepository.findAllById(req.departmentIds());
                for (Department department : departments) {
                    EmployeeDepartment assignment = EmployeeDepartment.builder()
                            .employee(e)
                            .department(department)
                            .build();
                    employeeDepartmentRepository.save(assignment);
                }
            }
        }

        employeeRepository.save(e);
        return toResponse(e);
    }

    public void delete(Long id) {
        Employee e = getById(id);

        fileStorageService.deleteProfilePicture(e.getProfilePictureUrl());

        employeeDepartmentRepository.deleteByEmployeeId(id);

        employeeRepository.delete(e);
    }


    public EmployeeResponse updateProfilePicture(Long id, MultipartFile file) throws IOException {
        Employee e = getById(id);

        // Delete old picture (if any)
        fileStorageService.deleteProfilePicture(e.getProfilePictureUrl());

        // Store new picture under uploads/profile-pictures/employees/{id}/...
        String url = fileStorageService.storeEmployeeProfilePicture(id, file);
        e.setProfilePictureUrl(url);

        employeeRepository.save(e);
        return toResponse(e);
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
}