package com.example.inventory.service;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.model.SupplierProfile;

import java.util.List;

public interface SupplierService {
    void deleteSupplier(Long id);

    List<SupplierDto> getAllSuppliers();

    SupplierProfile getSupplierProfileById(Long supplierId);

}
