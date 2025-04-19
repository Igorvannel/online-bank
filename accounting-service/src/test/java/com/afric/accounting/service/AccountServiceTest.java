package com.afric.accounting.service;

import com.afric.accounting.service.model.Account;
import com.afric.accounting.service.model.AccountingJournal;
import com.afric.accounting.service.repository.AccountRepository;
import com.afric.accounting.service.repository.AccountingJournalRepository;
import com.afric.accounting.service.service.AccountService;
import com.afric.common.constants.OperationType;
import com.afric.common.dto.request.TransactionRequestDto;
import com.afric.common.dto.response.TransactionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountingJournalRepository journalRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private AccountingJournal testJournal;
    private TransactionRequestDto creditRequest;
    private TransactionRequestDto debitRequest;

    @BeforeEach
    void setUp() {
        testAccount = new Account(
                "1",
                "user1",
                "ACC123456",
                new BigDecimal("1000.00"),
                "XAF",
                LocalDateTime.now(),
                LocalDateTime.now(),
                "SYSTEM",
                "SYSTEM"
        );

        testJournal = new AccountingJournal(
                "1",
                "1",
                new BigDecimal("100.00"),
                OperationType.credit(),
                new BigDecimal("1000.00"),
                new BigDecimal("1100.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "SYSTEM",
                "SYSTEM"
        );

        creditRequest = new TransactionRequestDto(
                "1",
                new BigDecimal("100.00")
        );

        debitRequest = new TransactionRequestDto(
                "1",
                new BigDecimal("50.00")
        );

        // Mock security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void creditAccount_Success() {
        // Arrange
        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(journalRepository.save(any(AccountingJournal.class))).thenReturn(testJournal);

        // Act
        TransactionDto result = accountService.creditAccount(creditRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testJournal.getId(), result.id());
        assertEquals(testJournal.getAccountId(), result.accountId());
        assertEquals(testJournal.getAmount(), result.amount());
        assertEquals(testJournal.getType().name(), result.direction());
        assertEquals(testJournal.getBalanceBefore(), result.balanceBefore());
        assertEquals(testJournal.getBalanceAfter(), result.balanceAfter());

        verify(accountRepository).findById("1");
        verify(accountRepository).save(any(Account.class));
        verify(journalRepository).save(any(AccountingJournal.class));
    }

    @Test
    void creditAccount_InvalidAmount() {
        // Arrange
        TransactionRequestDto invalidRequest = new TransactionRequestDto(
                "1",
                new BigDecimal("0.00")
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.creditAccount(invalidRequest);
        });

        verify(accountRepository, never()).findById(anyString());
        verify(accountRepository, never()).save(any(Account.class));
        verify(journalRepository, never()).save(any(AccountingJournal.class));
    }

    @Test
    void creditAccount_AccountNotFound() {
        // Arrange
        when(accountRepository.findById("1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.creditAccount(creditRequest);
        });

        verify(accountRepository).findById("1");
        verify(accountRepository, never()).save(any(Account.class));
        verify(journalRepository, never()).save(any(AccountingJournal.class));
    }

    @Test
    void debitAccount_Success() {
        // Arrange
        AccountingJournal debitJournal = new AccountingJournal(
                "2",
                "1",
                new BigDecimal("50.00"),
                OperationType.debit(),
                new BigDecimal("1000.00"),
                new BigDecimal("950.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "SYSTEM",
                "SYSTEM"
        );

        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(journalRepository.save(any(AccountingJournal.class))).thenReturn(debitJournal);

        // Act
        TransactionDto result = accountService.debitAccount(debitRequest);

        // Assert
        assertNotNull(result);
        assertEquals(debitJournal.getId(), result.id());
        assertEquals(debitJournal.getAccountId(), result.accountId());
        assertEquals(debitJournal.getAmount(), result.amount());
        assertEquals(debitJournal.getType().name(), result.direction());
        assertEquals(debitJournal.getBalanceBefore(), result.balanceBefore());
        assertEquals(debitJournal.getBalanceAfter(), result.balanceAfter());

        verify(accountRepository).findById("1");
        verify(accountRepository).save(any(Account.class));
        verify(journalRepository).save(any(AccountingJournal.class));
    }

    @Test
    void debitAccount_InsufficientBalance() {
        // Arrange
        TransactionRequestDto largeDebitRequest = new TransactionRequestDto(
                "1",
                new BigDecimal("1500.00")
        );

        when(accountRepository.findById("1")).thenReturn(Optional.of(testAccount));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            accountService.debitAccount(largeDebitRequest);
        });

        verify(accountRepository).findById("1");
        verify(accountRepository, never()).save(any(Account.class));
        verify(journalRepository, never()).save(any(AccountingJournal.class));
    }

    @Test
    void debitAccount_InvalidAmount() {
        // Arrange
        TransactionRequestDto invalidRequest = new TransactionRequestDto(
                "1",
                new BigDecimal("-10.00")
        );

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            accountService.debitAccount(invalidRequest);
        });

        verify(accountRepository, never()).findById(anyString());
        verify(accountRepository, never()).save(any(Account.class));
        verify(journalRepository, never()).save(any(AccountingJournal.class));
    }
}