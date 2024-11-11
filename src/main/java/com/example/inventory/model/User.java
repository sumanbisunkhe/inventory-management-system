package com.example.inventory.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_username", columnNames = "username"),
        @UniqueConstraint(name = "UNIQUE_email", columnNames = "email"),
        @UniqueConstraint(name = "UNIQUE_phone_number", columnNames = "phoneNumber")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Tag(name = "User", description = "Represents a system user with roles, personal details, and account status.")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "Username for the user", example = "john_doe", required = true)
    private String username;

    @Schema(description = "Password for the user account (hashed)", example = "hashedPassword123")
    private String password;

    @Column(unique = true, nullable = false)
    @Schema(description = "Email address for the user", example = "john.doe@example.com", required = true)
    private String email;

    @Schema(description = "Full name of the user", example = "John Doe")
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Schema(description = "Roles assigned to the user", implementation = Role.class)
    private Set<Role> roles = new HashSet<>();

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Schema(description = "User's date of birth: dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Column(length = 15)
    @Schema(description = "Phone number of the user", example = "+1234567890")
    private String phoneNumber;

    @Column(length = 255)
    @Schema(description = "Address of the user", example = "123 Main St, Springfield, IL")
    private String address;

    @JsonFormat(pattern = "EEEE, MMMM d, yyyy, h:mm a")
    @Schema(description = "Timestamp when the user account was created", example = "Monday, November 11, 2024, 2:20 PM")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "EEEE, MMMM d, yyyy, h:mm a")
    @Schema(description = "Timestamp when the user account was last updated", example = "Tuesday, November 12, 2024, 3:25 PM")
    private LocalDateTime updatedAt;

    @Schema(description = "Indicates if the user's account is active", example = "true")
    private Boolean isActive = true;

    // Supplier Profile: Additional details for suppliers
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Supplier profile associated with the user, available only if user is a supplier", implementation = SupplierProfile.class)
    private SupplierProfile supplierProfile;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (hasRole("SUPPLIER") && supplierProfile == null) {
            supplierProfile = new SupplierProfile();
            supplierProfile.setUser(this);
            supplierProfile.setName(this.getFullName());
            supplierProfile.setAddress(this.getAddress());
            supplierProfile.setContactNumber(this.getPhoneNumber());
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
}
