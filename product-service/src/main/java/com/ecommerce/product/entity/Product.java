package com.ecommerce.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JPA entity for product. Many-to-one with Category.
 * Timestamps set automatically on persist/update.
 **/
@Entity
@Table(name="products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 2000)
    private String description;
    @Column(nullable = false)
    private double price;
    private double discountPrice;
    private int quantity;
    private String brand;
    private String imageUrl;
    /**
     * Many products belong to one category. LAZY: category loaded
     * only when accessed.
     **/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    /**
     * Set created/updated timestamps when entity is first persisted.
     **/
    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    /**
     * Refresh updated timestamp on every update.
     **/
    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
