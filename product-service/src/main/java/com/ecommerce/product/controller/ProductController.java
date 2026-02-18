package com.ecommerce.product.controller;


import com.ecommerce.product.dto.ProductDto;
import com.ecommerce.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * REST controller for product CRUD, search, filter,
 * and image upload. Base path: /api/products
 **/
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    /**
     * Creates a new product.
     **/
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.createProduct(dto));
    }
    /**
     * Updates an existing product by ID.
     **/
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id,
                                                    @RequestBody ProductDto dto){
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }
    /**
     * Deletes a product by ID.
     **/
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product delete successfully");
    }
    /**
     * Returns a single product by ID.
     **/
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }
    /**
     * Returns paginated list of all products with sort options.
     **/
    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir){
        return ResponseEntity.ok(productService.getAllProduct(page, size, sortBy, sortDir));
    }
    /**
     * Searches products by keyword (name/description).
     **/
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(productService.searchProduct(keyword, page, size));
    }
    /**
     * Filters products by category and/or price range.
     **/
    @GetMapping("/filter")
    public ResponseEntity<Page<ProductDto>> filterProducts(
            @RequestParam(required = false)Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(productService.filterProducts(categoryId, minPrice, maxPrice, page, size));
    }
    /**
     * Advanced filter: keyword, category, price range, sort.
     **/
    @GetMapping("/advance-filter")
    public ResponseEntity<Page<ProductDto>> advanceFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return ResponseEntity.ok(productService.advanceFilter(keyword, categoryId, minPrice, maxPrice, page, size, sortBy, sortDir));
    }
    /**
     * Uploads an image for a product; returns updated product DTO.
     **/
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<ProductDto> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(productService.uploadImage(id, file));
    }
}
