package com.digitalbanking.backend.service;

import com.digitalbanking.backend.dto.response.AccountResponse;
import com.digitalbanking.backend.entity.Account;
import com.digitalbanking.backend.entity.Transaction;
import com.digitalbanking.backend.entity.User;
import com.digitalbanking.backend.enums.AccountType;
import com.digitalbanking.backend.enums.TransactionType;
import com.digitalbanking.backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    @Transactional
    public Transaction transferFunds(User currentUser, Long sourceAccountId, Long targetAccountId, BigDecimal amount, String description) {
        Account sourceAccount = accountService.getAccountById(sourceAccountId);
        Account targetAccount = accountService.getAccountById(targetAccountId);

        if (!sourceAccount.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("Source account does not belong to the current user");
        }

        if (sourceAccount.getId().equals(targetAccount.getId())) {
            throw new IllegalArgumentException("Transfer to the same account is not allowed");
        }

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setTargetAccount(targetAccount);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setStatus("COMPLETED");
        transaction.setDescription(description == null ? "Internal transfer" : description);

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsForAccount(Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return transactionRepository.findBySourceAccountOrTargetAccountOrderByCreatedAtDesc(account, account);
    }

    public List<AccountResponse> getAccountResponses(List<Account> accounts) {
        return accounts.stream().map(this::mapAccount).collect(Collectors.toList());
    }

    private AccountResponse mapAccount(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getStatus().name(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt().toString()
        );
    }
}
