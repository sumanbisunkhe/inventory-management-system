package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object (DTO) for Order.
 * Used for creating or updating an order in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    @Schema(description = "Unique identifier for the order", example = "1")
    private Long id;

    @NotNull(message = "Order date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Date and time when the order was placed", example = "2024-11-11 15:30:00", required = true)
    private LocalDateTime orderDate;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than zero")
    @Schema(description = "Total amount for the order", example = "1999.99", required = true)
    private BigDecimal totalAmount;

    @NotNull(message = "Products are required")
    @Schema(description = "List of product IDs included in the order", example = "[1, 2, 3]", required = true)
    private List<Long> productIds;

    @NotNull(message = "User ID is required")
    @Schema(description = "Unique identifier of the user who placed the order", example = "123", required = true)
    private Long userId;
}
