package com.example.userservice.dto.Children;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateChildrenRequest(
        @NotBlank String name,
        @Email String email,
        @NotNull Integer phoneNumber,
        @NotBlank String address,
        @NotBlank String departments
) { }
