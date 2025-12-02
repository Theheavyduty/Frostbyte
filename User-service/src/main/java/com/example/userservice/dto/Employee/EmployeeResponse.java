package com.example.userservice.dto.Employee;

public record EmployeeResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl
) { }
