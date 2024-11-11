package com.example.inventory.service;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.model.Order;
import com.example.inventory.model.Product;

import java.util.List;

public interface CSVService {
    void exportProductsToCSV(String filePath);
    List<ProductDto> importProductsFromCSV(String filePath);

    void exportOrdersToCSV(String filePath);
    List<OrderDto> importOrdersFromCSV(String filePath);
}
