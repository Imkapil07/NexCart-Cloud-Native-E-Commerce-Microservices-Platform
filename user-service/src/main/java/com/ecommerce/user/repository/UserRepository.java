package com.ecommerce.user.repository;

import com.ecommerce.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA repository for {@link com.ecommerce.user.entity.User}.
 * Provides lookup by email for login and duplicate-check on registration.
 **/
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Returns true if a user with the given email already exists.
     **/
    boolean existsByEmail(String email);
    /**
     * Finds user by email; empty if not found.
     **/
    Optional<User> findByEmail(String email);
}