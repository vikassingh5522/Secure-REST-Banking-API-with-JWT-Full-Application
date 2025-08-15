package com.banking.securebankingapi.repository;

import com.banking.securebankingapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Transaction entity.
 * Extends JpaRepository to provide CRUD operations and pagination for Transaction objects.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // No custom methods yet — inherits save, findById, findAll, delete, etc. from JpaRepository
}
