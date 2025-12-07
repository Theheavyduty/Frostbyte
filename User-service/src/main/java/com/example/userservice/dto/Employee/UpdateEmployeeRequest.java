package com.example.userservice.dto.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateEmployeeRequest(
        String name,
        @Email String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl
) { }