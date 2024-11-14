package com.example.inventory.controller;

import com.example.inventory.security.AuthenticationRequest;
import com.example.inventory.security.AuthenticationResponse;
import com.example.inventory.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthController authController;

    private AuthenticationRequest validRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Setup a valid authentication request
        validRequest = new AuthenticationRequest("user@example.com", "password123");

        // Mock a UserDetails object
        userDetails = mock(UserDetails.class);

        // Lenient stubbing to avoid UnnecessaryStubbingException
        lenient().when(userDetails.getUsername()).thenReturn("user@example.com");
        lenient().when(userDetails.getAuthorities()).thenReturn((Collection) List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }



    @Test
    void testLogin_Success() {
        // Mock successful authentication
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(validRequest.getIdentifier())).thenReturn(userDetails);
        when(jwtUtil.generateToken(anyString(), anyList())).thenReturn("mock-jwt-token");

        // Make the login request
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert the response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof AuthenticationResponse);
        AuthenticationResponse authenticationResponse = (AuthenticationResponse) response.getBody();
        assertEquals("mock-jwt-token", authenticationResponse.getJwt());
        assertEquals("Login successful", authenticationResponse.getMessage());

        // Verify interactions
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(validRequest.getIdentifier());
        verify(jwtUtil).generateToken(anyString(), anyList());
    }

    @Test
    void testLogin_Failure_InvalidCredentials() {
        // Mock failed authentication with AuthenticationException
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("Invalid credentials") {});

        // Make the login request
        ResponseEntity<?> response = authController.login(validRequest);

        // Assert the response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());

        // Verify interactions
        verify(authenticationManager).authenticate(any());
    }

}
