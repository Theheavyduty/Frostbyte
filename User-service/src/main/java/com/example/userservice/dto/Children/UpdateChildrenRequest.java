package com.example.userservice.dto.Children;

import jakarta.validation.constraints.Email;


public record UpdateChildrenRequest(
        String name,
        @Email String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        String departments
) { }

