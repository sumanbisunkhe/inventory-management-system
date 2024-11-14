package com.example.inventory.controller;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Create a new product
     * @param productDto Product details to create
     * @param bindingResult Validation errors if any
     * @return ResponseEntity with status and creation message
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        ProductDto createdProduct = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                String.format("Product '%s' created successfully with ID: %d.", createdProduct.getName(), createdProduct.getId()));
    }

    /**
     * Update an existing product by ID
     * @param productId The ID of the product to update
     * @param productDto Updated product details
     * @param bindingResult Validation errors if any
     * @return ResponseEntity with status and update message
     */
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable Long productId,
                                           @Valid @RequestBody ProductDto productDto,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ProductDto updatedProduct = productService.updateProduct(productId, productDto);
            return ResponseEntity.ok(
                    String.format("Product with ID: %d updated successfully. New name: '%s'.", updatedProduct.getId(), updatedProduct.getName()));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    /**
     * Get a product by ID
     * @param productId The ID of the product to retrieve
     * @return ResponseEntity with the product details
     */
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            ProductDto productDto = productService.getProductById(productId);
            return ResponseEntity.ok(productDto);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("Product not found with ID: %d. Please check the ID and try again.", productId));
        }
    }

    /**
     * Get all products
     * @return ResponseEntity with a list of all products
     */
    @GetMapping("/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Delete a product by ID
     * @param productId The ID of the product to delete
     * @return ResponseEntity with deletion message
     */
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(String.format("Product with ID: %d deleted successfully.", productId));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("Product not found with ID: %d. Unable to delete non-existent product.", productId));
        }
    }
}
