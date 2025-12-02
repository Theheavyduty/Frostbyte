package com.example.userservice.dto.Parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record UpdateParentRequest(

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

        String profilePictureUrl,

        @NotNull
        List<Long> childIds
) { }
