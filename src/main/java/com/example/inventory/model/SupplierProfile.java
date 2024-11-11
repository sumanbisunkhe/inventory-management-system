package com.example.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suppliers")
@Tag(name = "Supplier Profile", description = "Represents a supplier's profile, including their products and associated user.")
public class SupplierProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the supplier profile", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the supplier", example = "Acme Corp", required = true)
    private String name;

    @Schema(description = "Contact number of the supplier", example = "+1234567890")
    private String contactNumber;

    @Schema(description = "Physical address of the supplier", example = "456 Industrial Rd, Springfield, IL")
    private String address;

    @OneToMany(mappedBy = "supplierProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of products associated with the supplier", implementation = Product.class)
    private List<Product> products;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @Schema(description = "User associated with this supplier profile", implementation = User.class)
    private User user;
}
