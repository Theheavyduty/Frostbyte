package com.example.userservice.dto.Employee;

import jakarta.validation.constraints.NotBlank;

public record EmployeeLoginRequest(
        @NotBlank
        String name,

        @NotBlank
        String password
) { }
