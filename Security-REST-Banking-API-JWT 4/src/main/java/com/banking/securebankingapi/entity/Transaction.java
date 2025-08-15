package com.banking.securebankingapi.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;  // DEPOSIT, WITHDRAW, TRANSFER
    private double amount;
    @ManyToOne
    private Account fromAccount;
    @ManyToOne
    private Account toAccount;
    private LocalDateTime timestamp;
}