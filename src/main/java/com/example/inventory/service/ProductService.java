package com.example.inventory.service;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.model.Product;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

    ProductDto getProductById(Long id);

    List<ProductDto> getAllProducts();
}
