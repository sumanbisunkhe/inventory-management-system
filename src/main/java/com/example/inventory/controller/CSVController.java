package com.example.inventory.controller;

import com.example.inventory.dto.OrderDto;
import com.example.inventory.dto.ProductDto;
import com.example.inventory.service.CSVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Export Products to CSV",
            description = "Exports all products from the database to a CSV file at the specified file path.",
            tags = {"CSV Operations"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products exported successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/export/products")
    public ResponseEntity<String> exportProductsToCSV(
            @RequestParam String filePath) {
        csvService.exportProductsToCSV(filePath);
        return ResponseEntity.ok("Products exported to CSV successfully");
    }

    @Operation(
            summary = "Import Products from CSV",
            description = "Imports products from a CSV file into the database.",
            tags = {"CSV Operations"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products imported successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProductDto.class))),
            @ApiResponse(responseCode = "500", description = "Failed to import products", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CSV file containing product data",
            required = true,
            content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))
    )
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

    @Operation(
            summary = "Export Orders to CSV",
            description = "Exports all orders from the database to a CSV file at the specified file path.",
            tags = {"CSV Operations"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders exported successfully", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping("/export/orders")
    public ResponseEntity<String> exportOrdersToCSV(@RequestParam String filePath) {
        csvService.exportOrdersToCSV(filePath);
        return ResponseEntity.ok("Orders exported to CSV successfully");
    }

    @Operation(
            summary = "Import Orders from CSV",
            description = "Imports orders from a CSV file into the database.",
            tags = {"CSV Operations"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders imported successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDto.class))),
            @ApiResponse(responseCode = "500", description = "Failed to import orders", content = @Content)
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "CSV file containing order data",
            required = true,
            content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))
    )
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
