package com.example.frostbyte.checkservice.childstatus.api.requests;

import com.example.frostbyte.checkservice.childstatus.domain.KindergartenDepartment;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegisterFravaerRequest(
        @NotNull Long childId,
        @NotNull KindergartenDepartment department,
        @NotNull String absenceReasons,
        @NotNull Long employeeId
) {
}
