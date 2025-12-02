package com.example.userservice.dto.Parent;

import java.util.List;

public record ParentResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        List<Long> childIds
) { }
