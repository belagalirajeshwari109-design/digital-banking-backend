package com.digitalbanking.backend.service;

import com.digitalbanking.backend.entity.Account;
import com.digitalbanking.backend.entity.User;
import com.digitalbanking.backend.enums.AccountStatus;
import com.digitalbanking.backend.enums.AccountType;
import com.digitalbanking.backend.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createDefaultAccount(User user, AccountType accountType) {
        String accountNumber = generateRandomAccountNumber();
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setAccountType(accountType);
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);
        return accountRepository.save(account);
    }

    public List<Account> getAccountsForUser(User user) {
        return accountRepository.findByUser(user);
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found with id: " + accountId));
    }

    public Account createAccountForUser(User user, AccountType type) {
        return createDefaultAccount(user, type);
    }

    private String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
