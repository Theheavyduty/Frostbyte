package com.example.userservice.dto.Employee;


public record EmployeeWithPasswordResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        String passwordHash
) { }
