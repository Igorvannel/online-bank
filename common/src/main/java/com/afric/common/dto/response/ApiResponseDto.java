package com.afric.common.dto.response;

import java.time.LocalDateTime;

public record ApiResponseDto<T>(
        boolean success,
        String message,
        T data,
        LocalDateTime timestamp
) {
    // Constructor with default timestamp
    public ApiResponseDto(boolean success, String message, T data) {
        this(success, message, data, LocalDateTime.now());
    }

    // Static factory methods for common response patterns
    public static <T> ApiResponseDto<T> success(String message, T data) {
        return new ApiResponseDto<>(true, message, data);
    }

    public static <T> ApiResponseDto<T> error(String message) {
        return new ApiResponseDto<>(false, message, null);
    }
}