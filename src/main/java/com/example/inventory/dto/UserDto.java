package com.example.inventory.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Schema(description = "Unique identifier for the user", example = "1")
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Schema(description = "Username for the user", example = "john_doe", required = true)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    @Schema(description = "Password for the user account (hashed)", example = "P@ssw0rd123", required = true)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address for the user", example = "john.doe@example.com", required = true)
    private String email;

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name cannot exceed 100 characters")
    @Schema(description = "Full name of the user", example = "John Doe", required = true)
    private String fullName;

    @NotNull(message = "Roles are required")
    @Schema(description = "Roles assigned to the user", example = "[\"CUSTOMER\", \"SUPPLIER\"]", required = true)
    private Set<String> roles;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Date of birth must be a past date")
    @Schema(description = "Date of birth of the user in dd-MM-yyyy format", example = "01-01-1990", required = true)
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[+]?\\d{10,15}$", message = "Phone number should be valid and between 10-15 digits")
    @Schema(description = "Phone number of the user", example = "+123456789", required = true)
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    @Schema(description = "Address of the user", example = "1234 Elm Street, Springfield, IL", required = true)
    private String address;

    @Schema(description = "Indicates if the user's account is active", example = "true", required = false)
    private Boolean isActive;
    @JsonFormat(pattern = "EEEE, MMMM d, yyyy, h:mm a")
    @Schema(description = "Timestamp when the user account was created", example = "Monday, November 11, 2024, 2:20 PM")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "EEEE, MMMM d, yyyy, h:mm a")
    @Schema(description = "Timestamp when the user account was last updated", example = "Tuesday, November 12, 2024, 3:25 PM")
    private LocalDateTime updatedAt;
}
