package com.afric.common.dto;

import java.time.LocalDateTime;

public record UserDto(
        String id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {
    // Static factory method
    public static UserDto of(String id, String name, String email,
                             LocalDateTime createdAt, LocalDateTime updatedAt,
                             String createdBy, String updatedBy) {
        return new UserDto(id, name, email, createdAt, updatedAt, createdBy, updatedBy);
    }
}