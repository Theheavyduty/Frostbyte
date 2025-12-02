package com.example.userservice.dto;

public record UserResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl
) { }
