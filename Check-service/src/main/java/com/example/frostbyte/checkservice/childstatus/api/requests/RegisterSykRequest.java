package com.example.frostbyte.checkservice.childstatus.api.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegisterSykRequest(
       @NotNull Long childId,
       @NotNull LocalDateTime sicknessTime,
       @NotNull String symptoms,
       @NotNull Long employeeId
) {
}
