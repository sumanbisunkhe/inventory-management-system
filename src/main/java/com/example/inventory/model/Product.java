package com.example.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "products")
@Tag(name = "Product", description = "Represents a product, including its details, price, stock, and associated supplier.")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the product", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Name of the product", example = "Laptop", required = true)
    private String name;

    @Schema(description = "Description of the product", example = "15-inch laptop with 8GB RAM and 256GB SSD")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Price of the product", example = "999.99", required = true)
    private BigDecimal price;

    @Schema(description = "Quantity of the product in stock", example = "50")
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "supplier_profile_id", nullable = false)
    @Schema(description = "Supplier profile associated with this product", implementation = SupplierProfile.class)
    private SupplierProfile supplierProfile;

    @ManyToMany(mappedBy = "products")
    @Schema(description = "List of orders that include this product", implementation = Order.class)
    private List<Order> orders;
}
