package com.example.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateUserRequest(
        @NotBlank
        String name,

        @Email
        @NotBlank
        String email,

        @NotNull
        @Positive
        Integer phoneNumber,

        @NotBlank
        String address,

        String profilePictureUrl
) { }
