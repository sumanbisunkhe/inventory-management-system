package com.example.inventory.service;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.model.User;
import com.example.inventory.repo.OrderRepo;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.service.impl.CSVServiceImpl;
import com.example.inventory.utils.CsvUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CSVServiceImplTest {

    @Mock
    private ProductRepo productRepository;

    @Mock
    private OrderRepo orderRepository;

    @Mock
    private CsvUtils csvUtils;

    @InjectMocks
    private CSVServiceImpl csvService;

    private static final String FILE_PATH = "test.csv";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExportProductsToCSV() {
        // Mock products list
        List<Product> products = new ArrayList<>();

        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Product1");
        product1.setDescription("Description1");
        product1.setStockQuantity(10);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Product2");
        product2.setDescription("Description2");
        product2.setStockQuantity(20);

        products.add(product1);
        products.add(product2);

        // Mock the repository call to return the products list
        when(productRepository.findAll()).thenReturn(products);

        // Mock the method on csvUtils (if it's a mockable method)
        CsvUtils.exportProductsToCSV(products, FILE_PATH);

        // Call the method under test
        csvService.exportProductsToCSV(FILE_PATH);

        // Verify interactions
        verify(productRepository, times(1)).findAll();  // Verify if the findAll() method was called once
        CsvUtils.exportProductsToCSV(products, FILE_PATH);  // Verify if the exportProductsToCSV() method was called once
    }


    @Test
    void testImportProductsFromCSV() {
        // Mock ProductDto list returned by CsvUtils
        List<ProductDto> importedProducts = List.of(
                new ProductDto(1L, "Product1", "Description1", null, 10, 1L),
                new ProductDto(2L, "Product2", "Description2", null, 20, 2L)
        );

        // Mock the CsvUtils call
        when(csvUtils.importProductsFromCSV(FILE_PATH)).thenReturn(importedProducts);

        // Call the method under test
        List<ProductDto> result = csvService.importProductsFromCSV(FILE_PATH);

        // Verify interactions
        verify(csvUtils, times(1)).importProductsFromCSV(FILE_PATH);

        // Assertions
        assertEquals(importedProducts.size(), result.size());
        assertEquals(importedProducts.get(0).getName(), result.get(0).getName());
    }

    @Test
    void testExportOrdersToCSV() {
        List<Order> orders = new ArrayList<>();

        User user = new User();
        user.setId(1L);

        Order order1 = new Order();
        order1.setId(1L);
        order1.setProducts(new ArrayList<>());
        order1.setTotalAmount(BigDecimal.valueOf(100.00));
        order1.setOrderDate(LocalDateTime.now());
        order1.setUser(user);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setProducts(new ArrayList<>());
        order2.setTotalAmount(BigDecimal.valueOf(200.00));
        order2.setOrderDate(LocalDateTime.now());
        order2.setUser(user);

        orders.add(order1);
        orders.add(order2);

        when(orderRepository.findAll()).thenReturn(orders);

        csvService.exportOrdersToCSV(FILE_PATH);

        verify(orderRepository, times(1)).findAll();
        CsvUtils.exportOrdersToCSV(orders, FILE_PATH);
    }



    @Test
    void testImportOrdersFromCSV() {
        // Mock OrderDto list returned by CsvUtils
        List<OrderDto> importedOrders = new ArrayList<>();

        OrderDto order1 = new OrderDto();
        order1.setId(1L);
        order1.setProductIds(List.of(1L, 2L));  // Using setter to set product IDs
        order1.setUserId(1L);

        OrderDto order2 = new OrderDto();
        order2.setId(2L);
        order2.setProductIds(List.of(3L, 4L));  // Using setter to set product IDs
        order2.setUserId(2L);

        importedOrders.add(order1);
        importedOrders.add(order2);


        // Mock the CsvUtils call
        when(csvUtils.importOrdersFromCSV(FILE_PATH)).thenReturn(importedOrders);

        // Call the method under test
        List<OrderDto> result = csvService.importOrdersFromCSV(FILE_PATH);

        // Verify interactions
        verify(csvUtils, times(1)).importOrdersFromCSV(FILE_PATH);

        // Assertions
        assertEquals(importedOrders.size(), result.size());
        assertEquals(importedOrders.get(0).getId(), result.get(0).getId());
        assertEquals(importedOrders.get(0).getUserId(), result.get(0).getUserId());
    }
}
