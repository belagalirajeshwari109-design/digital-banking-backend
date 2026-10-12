package com.digitalbanking.backend.controller;

import com.digitalbanking.backend.dto.request.TransferRequest;
import com.digitalbanking.backend.dto.response.TransactionResponse;
import com.digitalbanking.backend.dto.response.UserResponse;
import com.digitalbanking.backend.entity.Transaction;
import com.digitalbanking.backend.entity.User;
import com.digitalbanking.backend.service.TransactionService;
import com.digitalbanking.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @PostMapping("/transactions/transfer")
    public ResponseEntity<TransactionResponse> transferFunds(
            Authentication authentication,
            @Valid @RequestBody TransferRequest request) {
        User user = userService.getCurrentUser(authentication.getName());
        Transaction transaction = transactionService.transferFunds(
                user,
                request.getSourceAccountId(),
                request.getTargetAccountId(),
                request.getAmount(),
                request.getDescription()
        );

        TransactionResponse response = new TransactionResponse(
                transaction.getId(),
                transaction.getType().name(),
                transaction.getStatus(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getAccountNumber() : null,
                transaction.getTargetAccount() != null ? transaction.getTargetAccount().getAccountNumber() : null,
                transaction.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(Authentication authentication) {
        User user = userService.getCurrentUser(authentication.getName());
        List<TransactionResponse> transactions = user.getAccounts().stream()
                .flatMap(account -> transactionService.getTransactionsForAccount(account.getId()).stream())
                .distinct()
                .map(transaction -> new TransactionResponse(
                        transaction.getId(),
                        transaction.getType().name(),
                        transaction.getStatus(),
                        transaction.getAmount(),
                        transaction.getDescription(),
                        transaction.getSourceAccount() != null ? transaction.getSourceAccount().getAccountNumber() : null,
                        transaction.getTargetAccount() != null ? transaction.getTargetAccount().getAccountNumber() : null,
                        transaction.getCreatedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(transactions);
    }
}
