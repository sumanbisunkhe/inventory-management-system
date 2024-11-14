package com.example.inventory.service;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.exceptions.ProductNotFoundException;
import com.example.inventory.model.Product;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.service.impl.ProductServiceImpl;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private SupplierService supplierService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private SupplierProfile supplierProfile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock product
        product = new Product();
        product.setId(1L);
        product.setName("Product One");
        product.setDescription("Description for Product One");
        product.setPrice(new BigDecimal("100.00"));
        product.setStockQuantity(10);

        // Set up mock supplier
        supplierProfile = new SupplierProfile();
        supplierProfile.setId(1L);
        supplierProfile.setName("Supplier One");

        // Set up mock product DTO
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("Product One");
        productDto.setDescription("Description for Product One");
        productDto.setPrice(new BigDecimal("100.00"));
        productDto.setStockQuantity(10);
        productDto.setSupplierId(1L);
    }

    @Test
    public void testCreateProduct_Success() throws InvocationTargetException, IllegalAccessException {
        // Arrange: Mock supplier service to return a supplier profile
        SupplierProfile supplierProfile = new SupplierProfile();
        supplierProfile.setId(1L);  // Mocking the supplier profile with a valid ID
        when(supplierService.getSupplierProfileById(productDto.getSupplierId())).thenReturn(supplierProfile);

        // Mock product repository to save the product and return the saved product
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        product.setSupplierProfile(supplierProfile); // Set the supplier profile

        when(productRepo.save(any(Product.class))).thenReturn(product);

        // Act: Call create product method
        ProductDto createdProductDto = productService.createProduct(productDto);

        // Assert: Verify interactions and assert results
        verify(productRepo, times(1)).save(any(Product.class));
        assertNotNull(createdProductDto);
        assertEquals(product.getName(), createdProductDto.getName());
        assertEquals(product.getDescription(), createdProductDto.getDescription());
        assertEquals(product.getPrice(), createdProductDto.getPrice());
        assertEquals(product.getStockQuantity(), createdProductDto.getStockQuantity());
        assertEquals(supplierProfile.getId(), createdProductDto.getSupplierId());
    }

    @Test
    public void testUpdateProduct_Success() {
        // Initialize the product object correctly
        Product product = new Product();
        product.setId(1L);
        product.setName("Product One");
        product.setDescription("Description for Product One");
        product.setPrice(BigDecimal.valueOf(100.00));
        product.setStockQuantity(10);

        // Initialize the SupplierProfile for the product
        SupplierProfile supplierProfile = new SupplierProfile();
        supplierProfile.setId(1L);
        product.setSupplierProfile(supplierProfile);

        // Mock productRepo.findById to return the existing product
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));

        // Mock productRepo.save to return the updated product
        when(productRepo.save(any(Product.class))).thenReturn(product);

        // Arrange: Create a valid productDto to update the product
        ProductDto productDto = new ProductDto();
        productDto.setName("Updated Product");
        productDto.setDescription("Updated Description");
        productDto.setPrice(BigDecimal.valueOf(150.00));
        productDto.setStockQuantity(20);
        productDto.setSupplierId(1L);

        // Mock supplier service to return the supplier profile for update
        when(supplierService.getSupplierProfileById(productDto.getSupplierId())).thenReturn(supplierProfile);

        // Act: Call update product method
        ProductDto updatedProductDto = productService.updateProduct(1L, productDto);

        // Assert: Verify interactions and assert results
        verify(productRepo, times(1)).save(any(Product.class));  // Ensure save is called once
        assertNotNull(updatedProductDto);  // Assert that the updated product DTO is not null
        assertEquals(productDto.getName(), updatedProductDto.getName());  // Check the name
        assertEquals(productDto.getDescription(), updatedProductDto.getDescription());  // Check the description
        assertEquals(productDto.getPrice(), updatedProductDto.getPrice());  // Check the price
        assertEquals(productDto.getStockQuantity(), updatedProductDto.getStockQuantity());  // Check the stock quantity
    }


    @Test
    public void testUpdateProduct_ProductNotFound() {
        // Mock the product repository to return empty (product not found)
        when(productRepo.findById(product.getId())).thenReturn(Optional.empty());

        // Call update product method and assert exception
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.updateProduct(product.getId(), productDto));
        assertEquals("Product not found for ID: " + product.getId(), exception.getMessage());
    }


    @Test
    public void testDeleteProduct_Success() {
        // Mock the product repository to return the product
        when(productRepo.findById(product.getId())).thenReturn(Optional.of(product));

        // Call delete product method
        productService.deleteProduct(product.getId());

        // Verify interactions
        verify(productRepo, times(1)).delete(any(Product.class));
    }

    @Test
    public void testDeleteProduct_ProductNotFound() {
        // Mock the product repository to return empty (product not found)
        when(productRepo.findById(product.getId())).thenReturn(Optional.empty());

        // Call delete product method and assert exception
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.deleteProduct(product.getId()));
        assertEquals("Product not found with id: " + product.getId(), exception.getMessage());
    }

    @Test
    public void testGetProductById_Success() {
        // Mock the product repository to return the product
        when(productRepo.findById(product.getId())).thenReturn(Optional.of(product));

        // Call get product by ID method
        ProductDto retrievedProductDto = productService.getProductById(product.getId());

        // Verify interactions and assert results
        verify(productRepo, times(1)).findById(product.getId());
        assertNotNull(retrievedProductDto);
        assertEquals(product.getId(), retrievedProductDto.getId());
        assertEquals(product.getName(), retrievedProductDto.getName());
        assertEquals(product.getDescription(), retrievedProductDto.getDescription());
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        // Mock the product repository to return empty (product not found)
        when(productRepo.findById(product.getId())).thenReturn(Optional.empty());

        // Call get product by ID method and assert exception
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(product.getId()));
        assertEquals("Product not found with id: " + product.getId(), exception.getMessage());
    }

    @Test
    public void testGetAllProducts() {
        // Mock the supplier profile
        SupplierProfile supplierProfile = new SupplierProfile();
        supplierProfile.setId(1L);  // Set a valid ID for the supplier profile

        // Set the supplier profile on the product
        product.setSupplierProfile(supplierProfile);

        // Mock the product repository to return a list of products
        when(productRepo.findAll()).thenReturn(Arrays.asList(product));

        // Call get all products method
        List<ProductDto> productDtos = productService.getAllProducts();

        // Verify interactions and assert results
        assertNotNull(productDtos);
        assertEquals(1, productDtos.size());
        assertEquals(product.getName(), productDtos.get(0).getName());
        assertEquals(product.getDescription(), productDtos.get(0).getDescription());
        assertEquals(product.getPrice(), productDtos.get(0).getPrice());
        assertEquals(product.getStockQuantity(), productDtos.get(0).getStockQuantity());
    }

}
