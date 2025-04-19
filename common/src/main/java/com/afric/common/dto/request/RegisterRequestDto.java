package com.afric.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

public record RegisterRequestDto(
        @NotBlank @Size(min = 3, max = 50) String name,
        @NotBlank @Size(min = 3, max = 50) @Email String email,
        @NotBlank @Size(min = 6, max = 40) String password,
        Set<String> roles
) {
    // Constructor with default values
    public RegisterRequestDto {
        if (roles == null) {
            roles = new HashSet<>();
        }
    }

    // Constructor without roles (defaults to empty set)
    public RegisterRequestDto(String name, String email, String password) {
        this(name, email, password, new HashSet<>());
    }

    // Static factory methods
    public static RegisterRequestDto of(String name, String email, String password) {
        return new RegisterRequestDto(name, email, password);
    }

    public static RegisterRequestDto of(String name, String email, String password, Set<String> roles) {
        return new RegisterRequestDto(name, email, password, roles);
    }
}