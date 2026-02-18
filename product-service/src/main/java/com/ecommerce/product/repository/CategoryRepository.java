package com.ecommerce.product.repository;

import com.ecommerce.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * JPA repository for {@link com.ecommerce.product.entity.Category}.
 * Supports parent/child hierarchy via parentId.
 **/
public interface CategoryRepository extends JpaRepository<Category, Long> {
    /**
     * Finds child categories by parent ID
     * (e.g. Electronics → parent; Mobile, Laptop → children).
     **/
    List<Category> findByParentId(Long parentId);
    /**
     * Returns true if a category with the given name already exists.
     **/
    boolean existsByName(String name);
}