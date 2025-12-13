package com.example.userservice.dto.Department;

import jakarta.validation.constraints.NotBlank;

public record CreateDepartmentRequest(
        @NotBlank String name
) { }