package com.example.frostbyte.checkservice.childstatus.api.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateChildStatusRequest(
        @NotNull LocalDateTime eventTime
) {}