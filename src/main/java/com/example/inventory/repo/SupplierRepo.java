package com.example.inventory.repo;

import com.example.inventory.model.SupplierProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepo extends JpaRepository<SupplierProfile, Long> {
}
