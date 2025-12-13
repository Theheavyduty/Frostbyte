package com.example.userservice.dto.Children;

import java.time.LocalDate;
import java.util.List;

public record ChildrenResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        DepartmentSummary department,
        LocalDate birthday,
        String profilePictureUrl,
        String additionalNotes,
        List<ParentRelationshipSummary> parents
) {
    public record DepartmentSummary(
            Long id,
            String name
    ) { }

    public record ParentRelationshipSummary(
            Integer registrationNumber,
            Long parentId,
            String name,
            String email,
            Integer phoneNumber
    ) { }
}