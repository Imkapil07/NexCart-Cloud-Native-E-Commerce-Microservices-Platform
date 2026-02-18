package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link CategoryService}: category CRUD.
 **/
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;
    /**
     * Creates category and saves to repository.
     **/
    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category  = categoryMapper.toEntity(dto);
        return categoryMapper.toDto(categoryRepo.save(category));
    }

    /**
     * Updates category by ID; throws if not found.
     **/
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepo.findById(id).orElseThrow(()-> new RuntimeException("category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        return categoryMapper.toDto(categoryRepo.save(category));
    }

    /**
     * Deletes category by ID.
     **/
    @Override
    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        return categoryRepo.findById(id).map(categoryMapper::toDto).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    /**
     * Returns all categories as DTOs.
     **/
    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll()
                .stream().map(categoryMapper::toDto).toList();
    }

}