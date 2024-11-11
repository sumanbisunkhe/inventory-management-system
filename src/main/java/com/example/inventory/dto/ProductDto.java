package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Data Transfer Object (DTO) for Product.
 * Used for creating or updating product details in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Product name cannot exceed 100 characters")
    @Schema(description = "Name of the product", example = "Laptop", required = true)
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Description of the product", example = "15-inch laptop with 8GB RAM and 256GB SSD")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    @Schema(description = "Price of the product", example = "999.99", required = true)
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    @Schema(description = "Quantity of the product in stock", example = "50", required = true)
    private Integer stockQuantity;

    @NotNull(message = "Supplier ID is required")
    @Schema(description = "Unique identifier of the supplier associated with this product", example = "1", required = true)
    private Long supplierId;
}
