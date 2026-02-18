package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service contract for product operations: CRUD,
 * search, filter, and image upload.
 **/
public interface ProductService {
    /**
     * Creates a new product.
     **/
    ProductDto createProduct(ProductDto dto);
    /**
     * Updates an existing product by ID.
     **/
    ProductDto updateProduct(Long id, ProductDto dto);
    /**
     * Deletes a product by ID.
     **/
    void deleteProduct(Long id);
    /**
     * Returns product by ID; throws if not found.
     **/
    ProductDto getProductById(Long id);
    /**
     * Uploads image for a product; returns updated product DTO.
     **/
    ProductDto uploadImage(Long productId, MultipartFile file) throws IOException;
    /**
     * Returns paginated products with sort.
     **/
    Page<ProductDto> getAllProduct(int page, int size, String sortBy, String sortDir);
    /**
     * Searches products by keyword.
     **/
    Page<ProductDto> searchProduct(String keyword, int page,int size);
    /**
     * Filters by category and/or price range.
     **/
    Page<ProductDto> filterProducts(Long categoryId,Double minPrice, Double maxPrice, int page, int size);
    /**
     * Advanced filter: keyword, category, price, sort.
     **/
    Page<ProductDto> advanceFilter(String keyword, Long categoryId, Double minPrice, Double maxPrice, int page, int size, String sortBy, String sortDir);
}