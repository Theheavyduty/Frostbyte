package com.example.userservice.dto.Parent;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateParentRequest(
        @NotBlank String name,
        @Email String email,
        @NotNull Integer phoneNumber,
        @NotBlank String address,
        List<Long> childIds
) {
}
