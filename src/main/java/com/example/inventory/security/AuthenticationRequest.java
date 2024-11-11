package com.example.inventory.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for user authentication request.
 * Contains user credentials for obtaining a JWT token.
 */
@Setter
@Getter
public class AuthenticationRequest {

    @NotBlank(message = "Identifier is required")
    @Schema(
            description = "User identifier which can be either username, email, or phone number",
            example = "johndoe@example.com"
    )
    private String identifier;

    @NotBlank(message = "Password is required")
    @Schema(
            description = "User's password for authentication",
            example = "SecureP@ssw0rd"
    )
    private String password;

    // Default constructor
    public AuthenticationRequest() {}

    // Parameterized constructor
    public AuthenticationRequest(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }
}
