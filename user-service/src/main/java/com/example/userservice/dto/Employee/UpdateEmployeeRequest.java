package com.example.userservice.dto.Employee;

import jakarta.validation.constraints.Email;

import java.util.List;

public record UpdateEmployeeRequest(
        String name,
        @Email String email,
        Integer phoneNumber,
        String address,
        List<Long> departmentIds
) { }