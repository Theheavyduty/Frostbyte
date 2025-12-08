package com.example.userservice.dto.Children;

import java.time.LocalDate;
import java.util.List;

public record ChildrenResponse(
        Long id,
        String name,
        String email,
        Integer phoneNumber,
        String address,
        String departments,
        LocalDate birthday,
        String profilePictureUrl,
        List<ParentSummary> parents
) {
    public record ParentSummary(
            Long id,
            String name,
            String email,
            Integer phoneNumber
    ) { }
}
