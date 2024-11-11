package com.example.inventory.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
@Tag(name = "Role", description = "Represents a role that a user can have within the system, such as ADMIN, SUPPLIER, etc.")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the role", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Name of the role", example = "ADMIN", required = true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    @Schema(description = "List of users associated with this role", implementation = User.class)
    private Set<User> users = new HashSet<>();
}
