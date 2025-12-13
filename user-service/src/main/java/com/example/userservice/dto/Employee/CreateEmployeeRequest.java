package com.example.userservice.dto.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateEmployeeRequest(
        @NotBlank String name,
        @Email String email,
        @NotBlank String password,
        @NotNull Integer phoneNumber,
        @NotBlank String address,
        List<Long> departmentIds
) { }