package com.example.userservice.dto.Parent;

import java.time.LocalDate;
import java.util.List;

public record ParentResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String profilePictureUrl,
        List<ChildRelationshipSummary> children
) {
    public record ChildRelationshipSummary(
            Integer registrationNumber,
            Long childId,
            String name,
            String email,
            Integer phoneNumber,
            DepartmentSummary department,
            LocalDate birthday
    ) { }

    public record DepartmentSummary(
            Long id,
            String name
    ) { }
}