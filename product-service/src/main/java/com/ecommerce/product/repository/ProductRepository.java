package com.ecommerce.product.repository;


import com.ecommerce.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * JPA repository for {@link com.ecommerce.product.entity.Product}.
 * Supports filtering, search, and advance filter via custom query.
 **/
public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * Finds products by category ID with pagination.
     **/
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
    /**
     * Finds products within price range with pagination.
     **/
    Page<Product> findByPriceBetween(Double min, Double max, Pageable pageable);
    /**
     * Finds products by name (case-insensitive) with pagination.
     **/
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    /**
     * Searches in name and description by keyword.
     **/
    @Query("SELECT p FROM Product p "+
            "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%')) "+
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword,'%'))")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Advance filter: optional keyword, category, price range.
     **/
    @Query("SELECT p FROM Product p "+
            "WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword,'%'))) "+
            "AND (:categoryId IS NULL OR p.category.id= :categoryId) "+
            "AND (p.price BETWEEN :minPrice AND :maxPrice)")
    Page<Product> advanceFilter(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}