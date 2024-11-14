package com.example.inventory.service.impl;


import com.example.inventory.model.Order;
import com.example.inventory.model.Product;
import com.example.inventory.repo.*;
import com.example.inventory.dto.UserDto;
import com.example.inventory.model.Role;
import com.example.inventory.model.User;
import com.example.inventory.service.EmailService;
import com.example.inventory.service.UserService;
import com.example.inventory.utils.CustomEmailMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepository;
    private final OrderRepo orderRepository;
    private final ProductRepo productRepository;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final SupplierRepo supplierRepo;

    @Override
    public UserDto registerUser(UserDto userDto) {
        // Encrypt the password
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // Get the current logged-in user's roles
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> currentUserRoles = auth.getAuthorities();

        // Check if the current user has ADMIN role
        boolean isAdmin = currentUserRoles.stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // Validate and set roles
        Set<Role> roles = new HashSet<>();
        if (isAdmin) {
            // Allow any role if the current user is an admin
            for (String roleName : userDto.getRoles()) {
                Role role = roleRepo.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
        } else {
            // Restrict roles to SUPPLIER or CUSTOMER for non-admin users
            if (userDto.getRoles().contains("ADMIN")) {
                throw new AccessDeniedException("Only an admin can assign the ADMIN role.");
            }
            for (String roleName : userDto.getRoles()) {
                if (!roleName.equals("SUPPLIER") && !roleName.equals("CUSTOMER")) {
                    throw new IllegalArgumentException("Invalid role for non-admin users.");
                }
                Role role = roleRepo.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                roles.add(role);
            }
        }

        // Map userDto to User entity and set roles
        User user = modelMapper.map(userDto, User.class);
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);

        // Save the user to the repository
        user = userRepository.save(user);

        // Prepare the email message
        CustomEmailMessage emailMessage = new CustomEmailMessage();
        emailMessage.setFrom("readymade090@gmail.com");
        emailMessage.setTo(user.getEmail());
        emailMessage.setSentDate(new Date());
        emailMessage.setSubject("Welcome to the Inventory Management System");
        emailMessage.setText(String.format(
                "Hello %s,\n\n" +
                        "Thank you for joining our Inventory Management System! We’re excited to help you efficiently manage and track inventory.\n\n" +
                        "With our system, you can streamline operations, stay organized, and make data-driven decisions to enhance productivity.\n\n" +
                        "Best regards,\n" +
                        "Inventory Management Team",
                user.getFullName()
        ));


        // Send registration success email
        emailService.sendEmail(emailMessage);

        return modelMapper.map(user, UserDto.class);
    }


    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        // Fetch the user to be updated
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        // Get the current authenticated user and their roles
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> currentUserRoles = auth.getAuthorities();
        boolean isAdmin = currentUserRoles.stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        // If the authenticated user is not an admin, prevent role changes to ADMIN
        if (!isAdmin && userDto.getRoles().contains("ADMIN")) {
            throw new AccessDeniedException("Only admins can assign or update the ADMIN role.");
        }

        // Update roles if provided in userDto
        Set<Role> updatedRoles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            Role role = roleRepo.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            updatedRoles.add(role);
        }

        // Update fields
        existingUser.setFullName(userDto.getFullName());
        existingUser.setEmail(userDto.getEmail());
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        existingUser.setRoles(updatedRoles);

        // Save and return updated user
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }



    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()) // Extracts the role name as a String
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);


        return userDto;
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> {
                    UserDto userDto = modelMapper.map(user, UserDto.class);

                    Set<String> roleNames = user.getRoles().stream()
                            .map(role -> role.getName()) // Extracts the role name as a String
                            .collect(Collectors.toSet());
                    userDto.setRoles(roleNames);

                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long userId) {
        // Fetch the user by id, throwing an exception if not found
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Fetch all orders related to the user
        Set<Order> userOrders = existingUser.getOrders();

        if (userOrders != null) {
            for (Order order : userOrders) {
                // Remove the product associations from the order
                // This only removes the link between orders and products, not the products themselves
                order.getProducts().clear();  // This removes the relationship between the order and the products

                // Delete the order itself (products should not be deleted)
                orderRepository.delete(order);  // Deletes the order, but leaves the products intact
            }
        }

        // Optionally, delete the supplier profile if applicable (if this is part of your business logic)
        if (existingUser.getSupplierProfile() != null) {
            supplierRepo.delete(existingUser.getSupplierProfile()); // If applicable
        }

        // Finally, delete the user
        userRepository.delete(existingUser);
        logger.info("Deleted user: {}", existingUser.getUsername());
    }



    @Override
    public UserDto activateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setIsActive(true);
        user = userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()) // Extracts the role name as a String
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);


        return userDto;
    }

    @Override
    public UserDto deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        user.setIsActive(false);
        user = userRepository.save(user);

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()) // Extracts the role name as a String
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);


        return userDto;
    }

    @Override
    public UserDto findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()) // Extracts the role name as a String
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);


        return userDto;
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        UserDto userDto = modelMapper.map(user, UserDto.class);

        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getName()) // Extracts the role name as a String
                .collect(Collectors.toSet());
        userDto.setRoles(roleNames);


        return userDto;
    }
}
