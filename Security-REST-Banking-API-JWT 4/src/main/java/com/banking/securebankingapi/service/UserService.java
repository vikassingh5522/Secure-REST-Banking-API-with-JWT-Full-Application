package com.banking.securebankingapi.service;

import com.banking.securebankingapi.entity.Account;
import com.banking.securebankingapi.entity.User;
import com.banking.securebankingapi.repository.AccountRepository;
import com.banking.securebankingapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User register(User user) {
        // Encode password and set default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");

        // Save user
        User savedUser = userRepository.save(user);

        // Only create an account if the user doesn't already have one
        accountRepository.findByUser(savedUser).ifPresentOrElse(
                existingAccount -> {
                    // Do nothing if account already exists
                },
                () -> {
                    Account account = new Account();
                    account.setUser(savedUser);
                    account.setBalance(0.0);
                    accountRepository.save(account);
                }
        );

        return savedUser;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
