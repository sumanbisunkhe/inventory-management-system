package com.example.inventory.controller;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.SupplierNotFoundException;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * Get all suppliers
     * @return List of all suppliers
     */
    @Operation(summary = "Get all suppliers", description = "Fetches a list of all suppliers in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of suppliers fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<SupplierDto>> getAllSuppliers() {
        List<SupplierDto> suppliers = supplierService.getAllSuppliers();
        return new ResponseEntity<>(suppliers, HttpStatus.OK);
    }

    /**
     * Get a supplier by ID
     * @param id The ID of the supplier to retrieve
     * @return The supplier's details
     */
    @Operation(summary = "Get supplier by ID", description = "Fetches a supplier's details by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found with the provided ID")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(
            @Parameter(description = "ID of the supplier to retrieve", required = true)
            @PathVariable Long id) {

        SupplierProfile supplierProfile = supplierService.getSupplierProfileById(id);
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplierProfile.getId());
        supplierDto.setName(supplierProfile.getName());
        supplierDto.setContactNumber(supplierProfile.getContactNumber());
        supplierDto.setAddress(supplierProfile.getAddress());

        return new ResponseEntity<>(supplierDto, HttpStatus.OK);
    }

    /**
     * Delete a supplier by ID
     * @param id The ID of the supplier to delete
     * @return Response message confirming the deletion
     */
    @Operation(summary = "Delete supplier by ID", description = "Deletes an existing supplier by their unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Supplier deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Supplier not found with the provided ID")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(
            @Parameter(description = "ID of the supplier to delete", required = true)
            @PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return new ResponseEntity<>("Supplier deleted successfully", HttpStatus.OK);
        } catch (SupplierNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
