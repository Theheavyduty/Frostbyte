package com.example.frostbyte.checkservice.childstatus.api.requests;

import com.example.frostbyte.checkservice.childstatus.domain.ChildStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegisterChildStatusRequest(
        @NotNull Long childId,
        @NotNull ChildStatus status,
        @NotNull Long employeeId
        ) {}
