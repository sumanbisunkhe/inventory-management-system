package com.example.inventory.controller;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.service.CSVService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CSVControllerTest {

    @Mock
    private CSVService csvService;

    @InjectMocks
    private CSVController csvController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(csvController).build();
    }

    @Test
    void testExportProductsToCSV() throws Exception {
        // Mock the service call
        doNothing().when(csvService).exportProductsToCSV(anyString());

        // Perform request and verify response
        mockMvc.perform(post("/api/csv/export/products")
                        .param("filePath", "/tmp/products.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string("Products exported to CSV successfully"));

        verify(csvService, times(1)).exportProductsToCSV("/tmp/products.csv");
    }

    @Test
    void testImportProductsFromCSV() throws Exception {
        // Mock the data
        ProductDto product = new ProductDto(1L, "Product1", "Description1", new BigDecimal("10.0"), 100, 1L);
        List<ProductDto> products = Collections.singletonList(product);

        // Mock service behavior
        when(csvService.importProductsFromCSV(anyString())).thenReturn(products);

        // Mock file
        MockMultipartFile file = new MockMultipartFile("file", "products.csv", "text/csv", "content".getBytes());

        // Perform request and verify response
        mockMvc.perform(multipart("/api/csv/import/products").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"name\":\"Product1\",\"description\":\"Description1\",\"price\":10.0,\"stockQuantity\":100,\"supplierId\":1}]"));

        verify(csvService, times(1)).importProductsFromCSV(anyString());
    }

    @Test
    void testExportOrdersToCSV() throws Exception {
        // Mock the service call
        doNothing().when(csvService).exportOrdersToCSV(anyString());

        // Perform request and verify response
        mockMvc.perform(post("/api/csv/export/orders")
                        .param("filePath", "/tmp/orders.csv"))
                .andExpect(status().isOk())
                .andExpect(content().string("Orders exported to CSV successfully"));

        verify(csvService, times(1)).exportOrdersToCSV("/tmp/orders.csv");
    }

    @Test
    void testImportOrdersFromCSV() throws Exception {
        // Mock the data
        LocalDateTime now = LocalDateTime.now();
        OrderDto order = new OrderDto(1L, now, new BigDecimal("50.0"), List.of(1L, 2L), 1L);
        List<OrderDto> orders = Collections.singletonList(order);

        // Mock service behavior
        when(csvService.importOrdersFromCSV(anyString())).thenReturn(orders);

        // Mock file
        MockMultipartFile file = new MockMultipartFile("file", "orders.csv", "text/csv", "content".getBytes());

        // Format date to match expected JSON format without 'T' separator
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = order.getOrderDate().format(formatter);


        // Perform request and verify response
        mockMvc.perform(multipart("/api/csv/import/orders").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"id\":1,\"orderDate\":\"" + formattedDate + "\",\"totalAmount\":50.0,\"productIds\":[1,2],\"userId\":1}]"));

        // Verify service method was called
        verify(csvService, times(1)).importOrdersFromCSV(anyString());
    }
}
