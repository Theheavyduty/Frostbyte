package com.example.userservice.dto.Children;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateChildrenRequest(
        @NotBlank String name,
        @Email String email,
        Integer phoneNumber,
        String address,
        @NotNull Long departmentId,
        LocalDate birthday,
        String additionalNotes
) { }