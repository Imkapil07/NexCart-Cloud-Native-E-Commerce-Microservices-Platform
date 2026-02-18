package com.ecommerce.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * JPA entity representing a user in the system.
 * Email is unique; password is stored encoded.
 * Timestamps are set automatically on persist/update.
 **/
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    private String phone;
    /**
     * Comma-separated or single role, e.g. ROLE_USER
     **/
    private String roles;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * Set created/updated timestamps when entity is first persisted.
     **/
    @PrePersist
    public void prePersist() {
        createdAt  = Instant.now();
        updatedAt = createdAt;
    }
    /**
     * Refresh updated timestamp on every update.
     **/
    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }
}
