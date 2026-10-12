package com.digitalbanking.backend.controller;

import com.digitalbanking.backend.dto.request.AccountCreateRequest;
import com.digitalbanking.backend.dto.response.AccountResponse;
import com.digitalbanking.backend.entity.Account;
import com.digitalbanking.backend.entity.User;
import com.digitalbanking.backend.enums.AccountType;
import com.digitalbanking.backend.service.AccountService;
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
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountResponse>> getAccounts(Authentication authentication) {
        User user = userService.getCurrentUser(authentication.getName());
        List<Account> accounts = accountService.getAccountsForUser(user);

        List<AccountResponse> responses = accounts.stream().map(account -> new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getStatus().name(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt().toString()
        )).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            Authentication authentication,
            @Valid @RequestBody AccountCreateRequest request) {
        User user = userService.getCurrentUser(authentication.getName());
        Account account = accountService.createAccountForUser(user, request.getAccountType());

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getStatus().name(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt().toString()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getAccountType().name(),
                account.getStatus().name(),
                account.getBalance(),
                account.getCurrency(),
                account.getCreatedAt().toString()
        );
        return ResponseEntity.ok(response);
    }
}
