package com.afric.common.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank @Email String email,
        @NotBlank String password
) {
    // Static factory method for easier creation
    public static LoginRequestDto of(String email, String password) {
        return new LoginRequestDto(email, password);
    }
}