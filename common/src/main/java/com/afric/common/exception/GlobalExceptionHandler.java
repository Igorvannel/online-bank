package com.afric.common.exception;

import com.afric.common.dto.response.ApiResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BankingException.class)
    public ResponseEntity<ApiResponseDto<Object>> handleBankingException(BankingException ex) {
        // Replace switch expression with traditional if-else approach
        Pair<ApiResponseDto<Object>, HttpStatus> responseAndStatus;

        if (ex instanceof ResourceNotFoundException) {
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error(ex.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        } else if (ex instanceof InsufficientBalanceException) {
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error(ex.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } else if (ex instanceof DuplicateResourceException) {
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error(ex.getMessage()),
                    HttpStatus.CONFLICT
            );
        } else if (ex instanceof AuthenticationException) {
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error(ex.getMessage()),
                    HttpStatus.UNAUTHORIZED
            );
        } else if (ex instanceof ValidationException) {
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error(ex.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } else {
            // Fallback for any unexpected BankingException subtype
            responseAndStatus = new Pair<>(
                    ApiResponseDto.error("Unexpected banking error occurred"),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        log.error("Banking exception: {}", ex.getMessage());
        return new ResponseEntity<>(responseAndStatus.getFirst(), responseAndStatus.getSecond());
    }

    // Simple Pair class for holding response and status together
    private static class Pair<T, U> {
        private final T first;
        private final U second;

        public Pair(T first, U second) {
            this.first = first;
            this.second = second;
        }

        public T getFirst() {
            return first;
        }

        public U getSecond() {
            return second;
        }
    }
}