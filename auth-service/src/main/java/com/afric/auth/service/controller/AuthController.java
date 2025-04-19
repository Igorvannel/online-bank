package com.afric.auth.service.controller;

import com.afric.auth.service.service.AuthService;
import com.afric.common.dto.UserDto;
import com.afric.common.dto.request.LoginRequestDto;
import com.afric.common.dto.request.RegisterRequestDto;
import com.afric.common.dto.response.ApiResponseDto;
import com.afric.common.dto.response.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account with USER role by default")
    public ResponseEntity<ApiResponseDto<UserDto>> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest) {
        UserDto userDto = authService.registerUser(registerRequest);

        ApiResponseDto<UserDto> response = new ApiResponseDto<>(
                true,
                "User registered successfully",
                userDto,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates credentials and returns JWT token")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        LoginResponseDto loginResponse = authService.authenticateUser(loginRequest);

        ApiResponseDto<LoginResponseDto> response = new ApiResponseDto<>(
                true,
                "Authentication successful",
                loginResponse,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Get current user", description = "Returns the authenticated user's details")
    public ResponseEntity<ApiResponseDto<UserDto>> getCurrentUser() {
        UserDto userDto = authService.getCurrentUser();

        ApiResponseDto<UserDto> response = new ApiResponseDto<>(
                true,
                "User details retrieved successfully",
                userDto,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}