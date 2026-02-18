package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryDto;

import java.util.List;

/**
 * Service contract for category CRUD operations.
 **/
public interface CategoryService {
    /**
     * Creates a new category.
     **/
    CategoryDto createCategory(CategoryDto dto);
    /**
     * Updates an existing category by ID.
     **/
    CategoryDto updateCategory(Long id, CategoryDto dto);
    /**
     * Deletes a category by ID.
     **/
    void deleteCategory(Long id);
    /**
     * Returns category by ID; throws if not found.
     **/
    CategoryDto getCategory(Long id);
    /**
     * Returns all categories.
     **/
    List<CategoryDto> getAllCategories();
}