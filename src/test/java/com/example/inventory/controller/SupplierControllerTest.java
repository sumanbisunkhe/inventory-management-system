package com.example.inventory.controller;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.GlobalExceptionHandler;
import com.example.inventory.exceptions.SupplierNotFoundException;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.service.SupplierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SupplierControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SupplierService supplierService;

    @Mock
    private GlobalExceptionHandler globalExceptionHandler;

    @InjectMocks
    private SupplierController supplierController;

    private SupplierProfile supplierProfile;
    private SupplierDto supplierDto;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Create a sample SupplierProfile and SupplierDto
        supplierProfile = new SupplierProfile(1L, "Test Supplier", "1234567890", "123 Supplier St", null, null);
        supplierDto = new SupplierDto();
        supplierDto.setId(supplierProfile.getId());
        supplierDto.setName(supplierProfile.getName());
        supplierDto.setContactNumber(supplierProfile.getContactNumber());
        supplierDto.setAddress(supplierProfile.getAddress());

        // Initialize MockMvc using MockMvcBuilders and explicitly add global exception handler
        mockMvc = MockMvcBuilders.standaloneSetup(supplierController)
                .setControllerAdvice(new GlobalExceptionHandler())  // Directly add GlobalExceptionHandler
                .build();
    }


    @Test
    void testGetAllSuppliers() throws Exception {
        // Mock the service method
        when(supplierService.getAllSuppliers()).thenReturn(Arrays.asList(supplierDto));

        // Perform the GET request
        mockMvc.perform(get("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(supplierDto.getId()))
                .andExpect(jsonPath("$[0].name").value(supplierDto.getName()))
                .andExpect(jsonPath("$[0].contactNumber").value(supplierDto.getContactNumber()))
                .andExpect(jsonPath("$[0].address").value(supplierDto.getAddress()));

        verify(supplierService, times(1)).getAllSuppliers();
    }

    @Test
    void testGetSupplierById() throws Exception {
        // Mock the service method
        when(supplierService.getSupplierProfileById(1L)).thenReturn(supplierProfile);

        // Perform the GET request
        mockMvc.perform(get("/api/suppliers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(supplierDto.getId()))
                .andExpect(jsonPath("$.name").value(supplierDto.getName()))
                .andExpect(jsonPath("$.contactNumber").value(supplierDto.getContactNumber()))
                .andExpect(jsonPath("$.address").value(supplierDto.getAddress()));

        verify(supplierService, times(1)).getSupplierProfileById(1L);
    }

    @Test
    void testGetSupplierById_NotFound() throws Exception {
        // Mock the service method to throw SupplierNotFoundException when supplier ID is not found
        when(supplierService.getSupplierProfileById(1L)).thenThrow(new SupplierNotFoundException("Supplier not found with id: 1"));

        // Perform the GET request to fetch the supplier by ID
        mockMvc.perform(get("/api/suppliers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())  // Expect 404 Not Found status
                .andExpect(jsonPath("$.error").value("Not Found"))  // Ensure the error field is "Not Found"
                .andExpect(jsonPath("$.message").value("Supplier not found with id: 1"))  // Ensure the exception message matches
                .andExpect(jsonPath("$.timestamp").exists())  // Ensure timestamp is present
                .andExpect(jsonPath("$.status").value(404));  // Ensure status is 404

        // Verify that the service method was called exactly once
        verify(supplierService, times(1)).getSupplierProfileById(1L);
    }




    @Test
    void testDeleteSupplier() throws Exception {
        // Mock the service method
        doNothing().when(supplierService).deleteSupplier(1L);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/suppliers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Supplier deleted successfully"));

        verify(supplierService, times(1)).deleteSupplier(1L);
    }

    @Test
    void testDeleteSupplier_NotFound() throws Exception {
        // Mock the service method to throw exception
        doThrow(new SupplierNotFoundException("Supplier not found with id: 1")).when(supplierService).deleteSupplier(1L);

        // Perform the DELETE request
        mockMvc.perform(delete("/api/suppliers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Supplier not found with id: 1"));

        verify(supplierService, times(1)).deleteSupplier(1L);
    }
}
