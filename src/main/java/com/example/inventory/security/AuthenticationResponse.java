package com.example.inventory.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
 * DTO for user authentication response.
 * Contains the JWT token and a message indicating authentication status.
 */
@Getter
public class AuthenticationResponse {

    @Schema(
            description = "JWT token generated after successful authentication",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    )
    private final String jwt;

    @Schema(
            description = "Message indicating the result of the authentication process",
            example = "Authentication successful"
    )
    private final String message;

    public AuthenticationResponse(String jwt, String message) {
        this.jwt = jwt;
        this.message = message;
    }
}
