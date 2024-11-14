package com.example.inventory.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Represents a product, including its details, price, stock, and associated supplier.
 */
@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "supplier_profile_id", nullable = false)
    private SupplierProfile supplierProfile;

    @ManyToMany(mappedBy = "products")
    private List<Order> orders;
}
