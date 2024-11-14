package com.example.inventory.service.impl;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.repo.OrderRepo;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.service.CSVService;
import com.example.inventory.utils.CsvUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CSVServiceImpl implements CSVService {
    private final ProductRepo productRepository;
    private final OrderRepo orderRepository;
    private final CsvUtils csvUtils;  // Inject CsvUtils as a non-static bean

    @Override
    public void exportProductsToCSV(String filePath) {
        try {
            log.info("Starting to export products to CSV: {}", filePath);
            List<Product> products = productRepository.findAll();
            csvUtils.exportProductsToCSV(products, filePath);  // Call non-static method through instance
            log.info("Successfully exported products to CSV: {}", filePath);
        } catch (Exception e) {
            log.error("Error exporting products to CSV", e);
        }
    }

    @Override
    public List<ProductDto> importProductsFromCSV(String filePath) {
        try {
            log.info("Starting to import products from CSV: {}", filePath);
            List<ProductDto> importedProducts = csvUtils.importProductsFromCSV(filePath);  // Call non-static method through instance
            log.info("Successfully imported products from CSV: {}", filePath);
            return importedProducts;
        } catch (Exception e) {
            log.error("Error importing products from CSV", e);
            return List.of(); // Return empty list in case of error
        }
    }

    @Override
    public void exportOrdersToCSV(String filePath) {
        try {
            log.info("Starting to export orders to CSV: {}", filePath);
            List<Order> orders = orderRepository.findAll();

            // Handle null values for product lists, totalAmount, orderDate, and user
            orders.forEach(order -> {
                if (order.getProducts() == null) {
                    order.setProducts(new ArrayList<>());
                }
                if (order.getTotalAmount() == null) {
                    order.setTotalAmount(BigDecimal.ZERO);  // Set a default amount if needed
                }
                if (order.getOrderDate() == null) {
                    order.setOrderDate(LocalDateTime.now());  // Set a default order date if needed
                }
                if (order.getUser() == null) {
                    log.warn("Order with ID {} has no associated user.", order.getId());
                }
            });

            csvUtils.exportOrdersToCSV(orders, filePath);
            log.info("Successfully exported orders to CSV: {}", filePath);
        } catch (Exception e) {
            log.error("Error exporting orders to CSV", e);
        }
    }




    @Override
    public List<OrderDto> importOrdersFromCSV(String filePath) {
        try {
            log.info("Starting to import orders from CSV: {}", filePath);
            List<OrderDto> importedOrders = csvUtils.importOrdersFromCSV(filePath);  // Call non-static method through instance
            log.info("Successfully imported orders from CSV: {}", filePath);
            return importedOrders;
        } catch (Exception e) {
            log.error("Error importing orders from CSV", e);
            return List.of(); // Return empty list in case of error
        }
    }
}
