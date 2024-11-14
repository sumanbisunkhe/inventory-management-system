package com.example.inventory.service;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.exceptions.SupplierNotFoundException;
import com.example.inventory.model.SupplierProfile;
import com.example.inventory.model.User;
import com.example.inventory.repo.SupplierRepo;
import com.example.inventory.repo.UserRepo;
import com.example.inventory.service.impl.SupplierServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierServiceImplTest {

    @Mock
    private SupplierRepo supplierRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    private SupplierProfile supplierProfile;
    private SupplierDto supplierDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Set up mock supplier profile
        supplierProfile = new SupplierProfile();
        supplierProfile.setId(1L);
        supplierProfile.setName("Supplier One");
        supplierProfile.setContactNumber("+1234567890");
        supplierProfile.setAddress("123 Supplier Street");

        // Set up mock supplier DTO
        supplierDto = new SupplierDto();
        supplierDto.setId(1L);
        supplierDto.setName("Supplier One");
        supplierDto.setContactNumber("+1234567890");
        supplierDto.setAddress("123 Supplier Street");
    }

    @Test
    public void testDeleteSupplier_Success() {
        // Mock the supplier repository to return an existing supplier
        when(supplierRepo.findById(supplierProfile.getId())).thenReturn(Optional.of(supplierProfile));

        // Mock the user repository (to check user deletion)
        User user = new User(); // assume User has some properties and constructors
        supplierProfile.setUser(user);

        // Call delete method
        supplierService.deleteSupplier(supplierProfile.getId());

        // Verify interactions with repositories
        verify(supplierRepo, times(1)).deleteById(supplierProfile.getId());
        verify(userRepo, times(1)).delete(user);
    }

    @Test
    public void testDeleteSupplier_SupplierNotFound() {
        // Mock the supplier repository to return empty for non-existing supplier
        when(supplierRepo.findById(supplierProfile.getId())).thenReturn(Optional.empty());

        // Call delete method and assert exception
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class,
                () -> supplierService.deleteSupplier(supplierProfile.getId()));
        assertEquals("Supplier not found with id: " + supplierProfile.getId(), exception.getMessage());
    }

    @Test
    public void testGetAllSuppliers() {
        // Mock the supplier repository
        when(supplierRepo.findAll()).thenReturn(Arrays.asList(supplierProfile));

        // Call get all suppliers method
        List<SupplierDto> supplierDtos = supplierService.getAllSuppliers();

        // Assert results
        assertNotNull(supplierDtos);
        assertEquals(1, supplierDtos.size());
        assertEquals(supplierProfile.getName(), supplierDtos.get(0).getName());
    }

    @Test
    public void testGetSupplierProfileById_Success() {
        // Mock the supplier repository to return the supplier profile
        when(supplierRepo.findById(supplierProfile.getId())).thenReturn(Optional.of(supplierProfile));

        // Call the method to fetch the supplier profile
        SupplierProfile result = supplierService.getSupplierProfileById(supplierProfile.getId());

        // Assert results
        assertNotNull(result);
        assertEquals(supplierProfile.getId(), result.getId());
    }

    @Test
    public void testGetSupplierProfileById_SupplierNotFound() {
        // Mock the supplier repository to return empty for non-existing supplier
        when(supplierRepo.findById(supplierProfile.getId())).thenReturn(Optional.empty());

        // Call the method and assert exception
        SupplierNotFoundException exception = assertThrows(SupplierNotFoundException.class,
                () -> supplierService.getSupplierProfileById(supplierProfile.getId()));
        assertEquals("Supplier not found with id: " + supplierProfile.getId(), exception.getMessage());
    }

    @Test
    public void testMapToDto() {
        // Map SupplierProfile entity to SupplierDto
        SupplierDto mappedDto = supplierService.mapToDto(supplierProfile);

        // Assert mapped DTO values
        assertEquals(supplierProfile.getId(), mappedDto.getId());
        assertEquals(supplierProfile.getName(), mappedDto.getName());
        assertEquals(supplierProfile.getContactNumber(), mappedDto.getContactNumber());
        assertEquals(supplierProfile.getAddress(), mappedDto.getAddress());
    }

    @Test
    public void testMapToEntity() {
        // Map SupplierDto to SupplierProfile entity
        SupplierProfile mappedEntity = supplierService.mapToEntity(supplierDto);

        // Assert mapped entity values
        assertEquals(supplierDto.getName(), mappedEntity.getName());
        assertEquals(supplierDto.getContactNumber(), mappedEntity.getContactNumber());
        assertEquals(supplierDto.getAddress(), mappedEntity.getAddress());
    }
}
