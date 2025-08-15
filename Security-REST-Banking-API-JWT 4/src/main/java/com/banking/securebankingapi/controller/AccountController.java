package com.banking.securebankingapi.controller;

import com.banking.securebankingapi.entity.User;
import com.banking.securebankingapi.service.AccountService;
import com.banking.securebankingapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
@CrossOrigin(origins = "http://localhost:5173") // Allow React frontend
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    // Get current logged-in user
    private User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userService.findByUsername(username);
    }

    // Get balance
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance() {
        return ResponseEntity.ok(accountService.getBalance(getCurrentUser()));
    }

    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody Map<String, Object> body) {
        Double amount = Double.valueOf(body.get("amount").toString());
        accountService.deposit(getCurrentUser(), amount);
        return ResponseEntity.ok("Deposit successful");
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, Object> body) {
        Double amount = Double.valueOf(body.get("amount").toString());
        accountService.withdraw(getCurrentUser(), amount);
        return ResponseEntity.ok("Withdraw successful");
    }

    // Transfer
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> body) {
        String toUsername = (String) body.get("toUsername");
        Double amount = Double.valueOf(body.get("amount").toString());
        accountService.transfer(getCurrentUser(), toUsername, amount);
        return ResponseEntity.ok("Transfer successful");
    }


}
