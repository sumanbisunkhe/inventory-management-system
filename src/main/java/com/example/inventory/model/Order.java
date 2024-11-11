package com.example.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
@Tag(name = "Order", description = "Represents a customer's order, including the products ordered, the user who placed the order, and the total amount.")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the order", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Date and time when the order was placed", example = "2024-11-11T15:30:00", required = true)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    @Schema(description = "Total amount for the order", example = "1999.99", required = true)
    private BigDecimal totalAmount;

    @ManyToMany
    @JoinTable(
            name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Schema(description = "List of products included in this order", implementation = Product.class)
    private List<Product> products;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "User who placed the order", implementation = User.class, required = true)
    private User user;
}
