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

        // Fetch the SupplierProfile based on the supplierId from the productDto
        SupplierProfile supplierProfile = supplierService.getSupplierProfileById(productDto.getSupplierId());

        if (supplierProfile != null) {
            // Log or assert to check if the supplierProfile is properly retrieved
            System.out.println("SupplierProfile fetched: " + supplierProfile.getId());
            product.setSupplierProfile(supplierProfile); // Set supplierProfile if not null
        } else {
            throw new IllegalArgumentException("Supplier not found for ID: " + productDto.getSupplierId());
        }

        // Save Product entity
        product = productRepo.save(product);

        // Convert back to DTO
        return mapToDto(product);
    }

    @Override
    public ProductDto updateProduct(Long productId, ProductDto productDto) {
        Product existingProduct = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for ID: " + productId));

        if (productDto == null) {
            throw new IllegalArgumentException("ProductDto must not be null");
        }

        BeanUtils.copyProperties(productDto, existingProduct);

        SupplierProfile supplierProfile = supplierService.getSupplierProfileById(productDto.getSupplierId());
        if (supplierProfile != null) {
            existingProduct.setSupplierProfile(supplierProfile);
        } else {
            throw new IllegalArgumentException("Supplier not found for ID: " + productDto.getSupplierId());
        }

        Product updatedProduct = productRepo.save(existingProduct);
        if (updatedProduct == null) {
            throw new IllegalStateException("Failed to save the updated product");
        }

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
        if (product == null) {
            return null; // Return null if product is null, or handle it as needed
        }

        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setStockQuantity(product.getStockQuantity());

        // Check if supplierProfile is null to avoid NullPointerException
        if (product.getSupplierProfile() != null) {
            productDto.setSupplierId(product.getSupplierProfile().getId());
        } else {
            productDto.setSupplierId(null);  // Handle the case when supplierProfile is null
        }

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
