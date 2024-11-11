package com.example.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Supplier Profile.
 * Used for creating or updating supplier profile details in the system.
 */
@Data
public class SupplierDto {

    @Schema(description = "Unique identifier for the supplier", example = "1")
    private Long id;

    @NotBlank(message = "Supplier name is required")
    @Size(min = 2, max = 100, message = "Supplier name must be between 2 and 100 characters")
    @Schema(description = "Name of the supplier", example = "Acme Corp", required = true)
    private String name;

    @NotNull(message = "Contact number is required")
    @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Contact number must be valid and contain between 7 and 15 digits")
    @Schema(description = "Contact number of the supplier", example = "+1234567890", required = true)
    private String contactNumber;

    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be 255 characters or fewer")
    @Schema(description = "Physical address of the supplier", example = "456 Industrial Rd, Springfield, IL", required = true)
    private String address;
}
