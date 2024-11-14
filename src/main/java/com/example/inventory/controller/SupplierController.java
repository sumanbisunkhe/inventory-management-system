package com.example.inventory.controller;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.SupplierNotFoundException;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.service.SupplierService;
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
    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable Long id) {
        // Attempt to get supplier and allow exception to propagate if not found
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return new ResponseEntity<>("Supplier deleted successfully", HttpStatus.OK);
        } catch (SupplierNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
