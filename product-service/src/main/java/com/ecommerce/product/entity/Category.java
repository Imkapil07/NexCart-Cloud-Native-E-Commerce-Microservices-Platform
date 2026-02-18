package com.ecommerce.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * JPA entity for category. Unique name; optional parentId for hierarchy.
 * One-to-many with Product.
 **/
@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
    /**
     * Parent category ID for hierarchy (null for root).
     **/
    private Long parentId;
    /**
     * Products in this category; cascade all operations.
     **/
    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> product;
}