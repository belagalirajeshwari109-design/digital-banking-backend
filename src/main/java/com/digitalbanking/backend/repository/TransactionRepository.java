package com.digitalbanking.backend.repository;

import com.digitalbanking.backend.entity.Account;
import com.digitalbanking.backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountOrTargetAccountOrderByCreatedAtDesc(Account sourceAccount, Account targetAccount);
}
