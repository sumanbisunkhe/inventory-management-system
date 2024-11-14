package com.example.inventory.utils;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.dto.OrderDto;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.model.User;
import com.example.inventory.repo.OrderRepo;
import com.example.inventory.repo.ProductRepo;
import com.example.inventory.repo.SupplierRepo;
import com.example.inventory.repo.UserRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CsvUtils {

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private OrderRepo orderRepository;
       @Autowired
    private UserRepo userRepository;
    @Autowired
    private SupplierRepo supplierRepo;

    // Export products to CSV file
    public static void exportProductsToCSV(List<Product> products, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"ID", "Name", "Description", "Price", "Stock Quantity"};
            writer.writeNext(header);

            for (Product product : products) {
                String price = (product.getPrice() != null) ? product.getPrice().toString() : "N/A";
                String[] data = {
                        product.getId().toString(),
                        product.getName(),
                        product.getDescription(),
                        price,
                        String.valueOf(product.getStockQuantity())
                };
                writer.writeNext(data);
            }
            log.info("Products exported successfully to CSV file: {}", filePath);
        } catch (IOException e) {
            log.error("Error exporting products to CSV", e);
        }
    }


    // Import products from CSV file and save them to the database
    public List<ProductDto> importProductsFromCSV(String filePath) {
        List<ProductDto> productDtos = new ArrayList<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.length < 6) continue; // Ensure there are 6 columns in the CSV

                // Create Product object and set values from the CSV
                Product product = new Product();
                product.setName(line[1]);
                product.setDescription(line[2]);
                product.setPrice(new BigDecimal(line[3]));
                product.setStockQuantity(Integer.parseInt(line[4]));

                // Fetch the SupplierProfile based on the supplierId from the CSV
                Long supplierId = Long.parseLong(line[5]);
                SupplierProfile supplierProfile = supplierRepo.findById(supplierId).orElse(null);

                if (supplierProfile != null) {
                    product.setSupplierProfile(supplierProfile);
                } else {
                    log.warn("Supplier with ID {} not found. Skipping product: {}", supplierId, product.getName());
                    continue; // Skip saving this product if supplier is not found
                }

                // Save product to DB
                product = productRepository.save(product);

                // Convert to ProductDto to return to the caller
                ProductDto productDto = new ProductDto();
                productDto.setId(product.getId());
                productDto.setName(product.getName());
                productDto.setDescription(product.getDescription());
                productDto.setPrice(product.getPrice());
                productDto.setStockQuantity(product.getStockQuantity());
                productDto.setSupplierId(product.getSupplierProfile().getId());

                productDtos.add(productDto);
            }
            log.info("Products imported and saved successfully from CSV file: {}", filePath);
        } catch (Exception e) {
            log.error("Error importing products from CSV", e);
        }
        return productDtos;
    }

    public static void exportOrdersToCSV(List<Order> orders, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            String[] header = {"Order ID", "Order Date", "Total Amount", "User ID", "Product IDs"};
            writer.writeNext(header);

            for (Order order : orders) {
                String productIds = order.getProducts().stream()
                        .map(product -> product.getId().toString())
                        .reduce((id1, id2) -> id1 + "," + id2)
                        .orElse("");

                // Check if user is not null before accessing user ID
                String userId = (order.getUser() != null) ? order.getUser().getId().toString() : "N/A";

                String[] data = {
                        order.getId().toString(),
                        order.getOrderDate().toString(),
                        order.getTotalAmount().toString(),
                        userId,
                        productIds
                };
                writer.writeNext(data);
            }
            log.info("Orders exported successfully to CSV file: {}", filePath);
        } catch (IOException e) {
            log.error("Error exporting orders to CSV", e);
        }
    }

    // Import orders from CSV file and save them to the database
    @Transactional
    public List<OrderDto> importOrdersFromCSV(String filePath) {
        List<OrderDto> orderDtos = new ArrayList<>();
        try (CSVReader reader = new CSVReader(Files.newBufferedReader(Paths.get(filePath)))) {
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                if (line.length < 5) continue;

                Order order = new Order();
                order.setOrderDate(LocalDateTime.parse(line[1]));
                order.setTotalAmount(new BigDecimal(line[2]));

                // Fetch the user based on UserId from the CSV
                Long userId = Long.parseLong(line[3]);
                User user = userRepository.findById(userId).orElse(null);
                if (user == null) {
                    log.warn("User with ID {} not found. Skipping order.", userId);
                    continue;
                }
                order.setUser(user);

                // Parse and set products
                String[] productIds = line[4].split(",");
                List<Product> products = new ArrayList<>();
                for (String productId : productIds) {
                    Product product = productRepository.findById(Long.parseLong(productId)).orElse(null);
                    if (product != null) {
                        products.add(product);
                    }
                }
                order.setProducts(products);

                // Save order to DB
                order = orderRepository.save(order);

                // Convert to OrderDto to return
                OrderDto orderDto = new OrderDto();
                orderDto.setId(order.getId()); // Capture the generated ID
                orderDto.setOrderDate(order.getOrderDate());
                orderDto.setTotalAmount(order.getTotalAmount());
                orderDto.setUserId(order.getUser().getId());

                // Convert product list to IDs
                List<Long> productIdsList = order.getProducts().stream()
                        .map(Product::getId)
                        .collect(Collectors.toList());
                orderDto.setProductIds(productIdsList);

                orderDtos.add(orderDto);
            }
            log.info("Orders imported successfully from CSV file: {}", filePath);
        } catch (Exception e) {
            log.error("Error importing orders from CSV", e);
        }
        return orderDtos;
    }


}
