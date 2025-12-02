package com.example.userservice.dto.Parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CreateParentRequest(

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

        String profilePictureUrl,

        @NotEmpty
        List<Long> childIds
) { }
