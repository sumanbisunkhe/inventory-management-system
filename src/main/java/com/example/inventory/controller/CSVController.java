package com.example.inventory.controller;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.service.CSVService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/csv")
@RequiredArgsConstructor
public class CSVController {

    private final CSVService csvService;

    /**
     * Export all products to a CSV file.
     *
     * @param filePath The file path where the CSV file will be saved.
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/export/products")
    public ResponseEntity<String> exportProductsToCSV(@RequestParam String filePath) {
        csvService.exportProductsToCSV(filePath);
        return ResponseEntity.ok("Products exported to CSV successfully");
    }

    /**
     * Import products from a CSV file.
     *
     * @param file The CSV file to import.
     * @return ResponseEntity containing the list of imported products.
     */
    @PostMapping("/import/products")
    public ResponseEntity<List<ProductDto>> importProductsFromCSV(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(filePath));
            List<ProductDto> products = csvService.importProductsFromCSV(filePath);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Export all orders to a CSV file.
     *
     * @param filePath The file path where the CSV file will be saved.
     * @return ResponseEntity with a success message.
     */
    @PostMapping("/export/orders")
    public ResponseEntity<String> exportOrdersToCSV(@RequestParam String filePath) {
        csvService.exportOrdersToCSV(filePath);
        return ResponseEntity.ok("Orders exported to CSV successfully");
    }

    /**
     * Import orders from a CSV file.
     *
     * @param file The CSV file to import.
     * @return ResponseEntity containing the list of imported orders.
     */
    @PostMapping("/import/orders")
    public ResponseEntity<List<OrderDto>> importOrdersFromCSV(@RequestParam("file") MultipartFile file) {
        try {
            String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
            file.transferTo(new java.io.File(filePath));
            List<OrderDto> orders = csvService.importOrdersFromCSV(filePath);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
