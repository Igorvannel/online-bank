package com.afric.accounting.service.service;

import com.afric.accounting.service.model.Account;
import com.afric.accounting.service.model.AccountingJournal;
import com.afric.accounting.service.repository.AccountRepository;
import com.afric.accounting.service.repository.AccountingJournalRepository;
import com.afric.common.constants.CreditOperation;
import com.afric.common.constants.DebitOperation;
import com.afric.common.constants.OperationType;
import com.afric.common.dto.request.TransactionRequestDto;
import com.afric.common.dto.response.TransactionDto;
import com.afric.common.exception.InsufficientBalanceException;
import com.afric.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountingJournalRepository journalRepository;

    @Transactional
    public TransactionDto processTransaction(TransactionRequestDto request, OperationType type) {

        if (type instanceof CreditOperation) {
            return creditAccount(request);
        } else if (type instanceof DebitOperation) {
            return debitAccount(request);
        } else {
            throw new IllegalArgumentException("Unsupported operation type");
        }
    }

    @Transactional
    public TransactionDto creditAccount(TransactionRequestDto request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", request.accountId()));

        BigDecimal balanceBefore = account.getBalance();
        BigDecimal newBalance = balanceBefore.add(request.amount());

        // Update account balance
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);

        // Create journal entry
        AccountingJournal journal = new AccountingJournal();
        journal.setAccountId(savedAccount.getId());
        journal.setAmount(request.amount());
        journal.setType(CreditOperation.getInstance());
        journal.setBalanceBefore(balanceBefore);
        journal.setBalanceAfter(newBalance);
        journal.setCreatedAt(LocalDateTime.now());
        AccountingJournal savedJournal = journalRepository.save(journal);

        return mapToTransactionDto(savedJournal);
    }

    @Transactional
    public TransactionDto debitAccount(TransactionRequestDto request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account", request.accountId()));

        BigDecimal balanceBefore = account.getBalance();

        // Check if enough balance
        if (balanceBefore.compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException(
                    account.getId(), request.amount(), balanceBefore);
        }

        BigDecimal newBalance = balanceBefore.subtract(request.amount());

        // Update account balance
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);

        // Create journal entry
        AccountingJournal journal = new AccountingJournal();
        journal.setAccountId(savedAccount.getId());
        journal.setAmount(request.amount());
        journal.setType(DebitOperation.getInstance());
        journal.setBalanceBefore(balanceBefore);
        journal.setBalanceAfter(newBalance);
        journal.setCreatedAt(LocalDateTime.now());
        AccountingJournal savedJournal = journalRepository.save(journal);

        return mapToTransactionDto(savedJournal);
    }

    private TransactionDto mapToTransactionDto(AccountingJournal journal) {
        return TransactionDto.of(
                journal.getId(),
                journal.getAccountId(),
                journal.getAmount(),
                journal.getType().name(),
                journal.getBalanceBefore(),
                journal.getBalanceAfter(),
                journal.getCreatedAt(),
                journal.getUpdatedAt(),
                journal.getCreatedBy(),
                journal.getUpdatedBy()
        );
    }
}