package com.ecommerce.product.service;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import com.ecommerce.product.mapper.CategoryMapper;
import com.ecommerce.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link CategoryService}: category CRUD.
 **/
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepo;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        log.info("[ProductService] createCategory: name={}", dto.getName());
        Category category = categoryMapper.toEntity(dto);
        Category saved = categoryRepo.save(category);
        log.info("[ProductService] createCategory OK: id={} name={}", saved.getId(), saved.getName());
        return categoryMapper.toDto(saved);
    }

    /**
     * Updates category by ID; throws if not found.
     **/
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        log.info("[ProductService] updateCategory: id={}", id);
        Category category = categoryRepo.findById(id).orElseThrow(() -> new RuntimeException("category not found"));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setParentId(dto.getParentId());
        return categoryMapper.toDto(categoryRepo.save(category));
    }

    @Override
    public void deleteCategory(Long id) {
        log.info("[ProductService] deleteCategory: id={}", id);
        categoryRepo.deleteById(id);
    }

    @Override
    public CategoryDto getCategory(Long id) {
        log.info("[ProductService] getCategory: id={}", id);
        return categoryRepo.findById(id).map(categoryMapper::toDto).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        log.info("[ProductService] getAllCategories");
        return categoryRepo.findAll().stream().map(categoryMapper::toDto).toList();
    }

}