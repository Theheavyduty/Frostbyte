package com.example.userservice.dto.Employee;

import java.util.List;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        List<DepartmentSummary> departments
) {
    public record DepartmentSummary(
            Long id,
            String name
    ) { }
}