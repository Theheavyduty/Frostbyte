package com.example.userservice.dto.Parent;

import jakarta.validation.constraints.Email;

import java.util.List;


public record UpdateParentRequest(
        String name,
        @Email String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        List<Long> childIds
) {
}
