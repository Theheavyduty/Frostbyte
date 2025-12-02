package com.example.userservice.dto.Employee;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateEmployeeRequest(

        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotNull
        @Positive
        Integer phoneNumber,

        @NotBlank
        String address,

        // optional picture URL (or filled after upload)
        String profilePictureUrl
) { }
