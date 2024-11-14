package com.example.inventory.controller;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private ProductController productController;

    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Sample Product");
        productDto.setDescription("This is a sample product.");
        productDto.setPrice(BigDecimal.valueOf(100.0));
        productDto.setStockQuantity(10);
        productDto.setSupplierId(1L);
    }

    @Test
    void testCreateProduct_Success() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.createProduct(any(ProductDto.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Product 'Sample Product' created successfully"));
    }

    @Test
    void testCreateProduct_ValidationError() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        ResponseEntity<?> response = productController.createProduct(productDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testUpdateProduct_Success() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.updateProduct(anyLong(), any(ProductDto.class))).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.updateProduct(1L, productDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("updated successfully"));
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        // Arrange
        when(bindingResult.hasErrors()).thenReturn(false);
        when(productService.updateProduct(anyLong(), any(ProductDto.class)))
                .thenThrow(new ProductNotFoundException("Product not found for ID: 1"));

        // Act
        ResponseEntity<?> response = productController.updateProduct(1L, productDto, bindingResult);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found for ID: 1", response.getBody());
    }

    @Test
    void testGetProductById_Success() {
        // Arrange
        when(productService.getProductById(anyLong())).thenReturn(productDto);

        // Act
        ResponseEntity<?> response = productController.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void testGetProductById_NotFound() {
        // Arrange
        when(productService.getProductById(anyLong()))
                .thenThrow(new ProductNotFoundException("Product not found with ID: 1"));

        // Act
        ResponseEntity<?> response = productController.getProductById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Product not found"));
    }

    @Test
    void testGetAllProducts() {
        // Arrange
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(productDto));

        // Act
        ResponseEntity<List<ProductDto>> response = productController.getAllProducts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testDeleteProduct_Success() {
        // Arrange
        doNothing().when(productService).deleteProduct(anyLong());

        // Act
        ResponseEntity<String> response = productController.deleteProduct(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("deleted successfully"));
    }

    @Test
    void testDeleteProduct_NotFound() {
        // Arrange
        doThrow(new ProductNotFoundException("Product not found with ID: 1")).when(productService).deleteProduct(anyLong());

        // Act
        ResponseEntity<String> response = productController.deleteProduct(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().contains("Product not found"));
    }
}
