package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.CategoryDto;
import com.ecommerce.product.entity.Category;
import org.springframework.stereotype.Component;

/**
 * Maps between Category entity and CategoryDto.
 **/
@Component
public class CategoryMapper {
    /**
     * Converts entity to DTO.
     **/
    public CategoryDto toDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .build();
    }
    /**
     * Converts DTO to entity.
     **/
    public Category toEntity(CategoryDto dto) {
        return Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .parentId(dto.getParentId())
                .build();
    }
}
