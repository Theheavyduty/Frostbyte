package com.example.frostbyte.checkservice.childstatus.api.response;

import com.example.frostbyte.checkservice.childstatus.domain.ChildStatus;

import java.time.LocalDateTime;

public record ChildStatusResponse(
        Long id,
        Long childId,
        ChildStatus status,
        LocalDateTime registertedAt,
        String symptoms,
        String absenceReasons
) {}
