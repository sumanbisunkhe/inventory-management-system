package com.example.inventory.service.impl;

import com.example.inventory.repo.ProductRepo;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.model.Product;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.service.ProductService;
import com.example.inventory.service.SupplierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final SupplierService supplierService;

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        // Map DTO to Entity
        Product product = mapToEntity(productDto);

        // Set the SupplierProfile entity from SupplierService
        SupplierProfile supplierProfile = supplierService.getSupplierProfileById(productDto.getSupplierId());
        product.setSupplierProfile(supplierProfile); // Assuming your Product entity has a SupplierProfile reference


        // Save Product entity
        product = productRepo.save(product);

        // Convert back to DTO
        return mapToDto(product);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        // Find existing Product or throw exception
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        // Update product details
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());

        // Update SupplierProfile
        SupplierProfile supplierProfile = supplierService.getSupplierProfileById(productDto.getSupplierId());
        existingProduct.setSupplierProfile(supplierProfile);

        // Save updated Product
        Product updatedProduct = productRepo.save(existingProduct);

        // Convert back to DTO
        return mapToDto(updatedProduct);
    }


    @Override
    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        productRepo.delete(product);
    }

    @Override
    public ProductDto getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return mapToDto(product);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProductDto mapToDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);
        productDto.setSupplierId(product.getSupplierProfile().getId()); // Update to match SupplierProfile
        return productDto;
    }

    private Product mapToEntity(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        return product;
    }

    private SupplierProfile convertToSupplierProfileEntity(SupplierDto supplierProfileDto) {
        SupplierProfile supplierProfile = new SupplierProfile();
        BeanUtils.copyProperties(supplierProfileDto, supplierProfile);
        return supplierProfile;
    }


}
