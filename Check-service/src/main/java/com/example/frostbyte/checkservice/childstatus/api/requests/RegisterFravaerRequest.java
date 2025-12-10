package com.example.frostbyte.checkservice.childstatus.api.requests;

import jakarta.validation.constraints.NotNull;

public record RegisterFravaerRequest(
        @NotNull Long childId,
        @NotNull String absenceReasons,
        @NotNull Long employeeId
) {
}
