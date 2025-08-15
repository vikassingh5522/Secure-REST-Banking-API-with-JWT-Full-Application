package com.banking.securebankingapi.service;

import com.banking.securebankingapi.entity.Account;
import com.banking.securebankingapi.entity.Transaction;
import com.banking.securebankingapi.entity.User;
import com.banking.securebankingapi.repository.AccountRepository;
import com.banking.securebankingapi.repository.TransactionRepository;
import com.banking.securebankingapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    public double getBalance(User user) {
        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        return account.getBalance();
    }

    @Transactional
    public void deposit(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setType("DEPOSIT");
        tx.setAmount(amount);
        tx.setFromAccount(account);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }

    @Transactional
    public void withdraw(User user, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdraw amount must be positive");
        }

        Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction tx = new Transaction();
        tx.setType("WITHDRAW");
        tx.setAmount(amount);
        tx.setFromAccount(account);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }

    @Transactional
    public void transfer(User fromUser, String toUsername, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        Account fromAccount = accountRepository.findByUser(fromUser)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        if (fromAccount.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        User toUser = userRepository.findByUsername(toUsername)
                .orElseThrow(() -> new RuntimeException("Recipient not found"));

        Account toAccount = accountRepository.findByUser(toUser)
                .orElseThrow(() -> new RuntimeException("Recipient account not found"));

        // Deduct from sender
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        accountRepository.save(fromAccount);

        // Add to recipient
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(toAccount);

        // Save transaction
        Transaction tx = new Transaction();
        tx.setType("TRANSFER");
        tx.setAmount(amount);
        tx.setFromAccount(fromAccount);
        tx.setToAccount(toAccount);
        tx.setTimestamp(LocalDateTime.now());
        transactionRepository.save(tx);
    }
}
