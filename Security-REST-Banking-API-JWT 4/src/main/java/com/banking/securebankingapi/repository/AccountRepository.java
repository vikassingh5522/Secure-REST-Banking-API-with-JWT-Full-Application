package com.banking.securebankingapi.repository;

import com.banking.securebankingapi.entity.Account;
import com.banking.securebankingapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository interface for Account entity.
 * Extends JpaRepository to provide CRUD operations and pagination for Account objects.
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Custom query method to find an account by its associated user.
     * @param user the User entity
     * @return Optional<Account> — account found or empty if none exists
     */
    Optional<Account> findByUser(User user);
}
