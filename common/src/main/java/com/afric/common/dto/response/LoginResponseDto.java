package com.afric.common.dto.response;

import java.util.Set;

public record LoginResponseDto(
        String token,
        String type,
        String id,
        String name,
        String email,
        Set<String> roles
) {
    // Constructor with default type
    public LoginResponseDto(String token, String id, String name, String email, Set<String> roles) {
        this(token, "Bearer", id, name, email, roles);
    }

    // Static factory method
    public static LoginResponseDto of(String token, String id, String name, String email, Set<String> roles) {
        return new LoginResponseDto(token, id, name, email, roles);
    }
}