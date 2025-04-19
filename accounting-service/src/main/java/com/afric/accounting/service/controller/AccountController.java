package com.afric.accounting.service.controller;

import com.afric.accounting.service.service.AccountService;
import com.afric.common.dto.request.TransactionRequestDto;
import com.afric.common.dto.response.ApiResponseDto;
import com.afric.common.dto.response.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account", description = "Account management API")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/credit")
    @Operation(
            summary = "Credit an account",
            description = "Adds funds to an account and creates a journal entry"
    )
    public ResponseEntity<ApiResponseDto<TransactionDto>> creditAccount(
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionDto transaction = accountService.creditAccount(request);

        ApiResponseDto<TransactionDto> response = new ApiResponseDto<>(
                true,
                "Account credited successfully",
                transaction,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/debit")
    @Operation(
            summary = "Debit an account",
            description = "Withdraws funds from an account and creates a journal entry"
    )
    public ResponseEntity<ApiResponseDto<TransactionDto>> debitAccount(
            @Valid @RequestBody TransactionRequestDto request) {
        TransactionDto transaction = accountService.debitAccount(request);

        ApiResponseDto<TransactionDto> response = new ApiResponseDto<>(
                true,
                "Account debited successfully",
                transaction,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}