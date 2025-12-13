package com.example.userservice.dto.auth;

import java.util.List;

public record LoginResponse(
        String username,
        List<String> roles
) {
}


