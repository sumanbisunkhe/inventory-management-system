package com.example.inventory.service.impl;

import com.example.inventory.model.User;
import com.example.inventory.repo.SupplierRepo;
import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.SupplierNotFoundException;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.repo.UserRepo;
import com.example.inventory.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepository;
    private final UserRepo userRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepo supplierRepository, UserRepo userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }


    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        // Check if supplier exists, otherwise throw exception
        SupplierProfile supplierProfile = supplierRepository.findById(id)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + id));

        // Delete the associated user, if any
        User user = supplierProfile.getUser();
        if (user != null) {
            userRepository.delete(user);
        }

        // Delete the supplier profile
        supplierRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SupplierDto> getAllSuppliers() {
        // Get all suppliers and map them to DTOs
        return supplierRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SupplierProfile getSupplierProfileById(Long supplierId) {
        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new SupplierNotFoundException("Supplier not found with id: " + supplierId));
    }



    // Map SupplierDto to SupplierProfile entity
    private SupplierProfile mapToEntity(SupplierDto supplierDto) {
        SupplierProfile supplier = new SupplierProfile();
        supplier.setName(supplierDto.getName());
        supplier.setContactNumber(supplierDto.getContactNumber());
        supplier.setAddress(supplierDto.getAddress());
        return supplier;
    }

    // Map SupplierProfile entity to SupplierDto
    private SupplierDto mapToDto(SupplierProfile supplier) {
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setId(supplier.getId());
        supplierDto.setName(supplier.getName());
        supplierDto.setContactNumber(supplier.getContactNumber());
        supplierDto.setAddress(supplier.getAddress());
        return supplierDto;
    }
}
