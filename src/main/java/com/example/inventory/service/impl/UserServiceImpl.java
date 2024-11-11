package com.example.inventory.service.impl;


import com.example.inventory.repo.RoleRepo;
import com.example.inventory.repo.UserRepo;
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
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        // Check if the currently authenticated user is allowed to assign ADMIN role
        Set<String> currentUserRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Ensure only an ADMIN can update a user's role to ADMIN
        if (userDto.getRoles().contains("ADMIN") && !currentUserRoles.contains("ROLE_ADMIN")) {
            throw new AccessDeniedException("Only ADMIN users can assign ADMIN role.");
        }

        // Update the password if it's provided
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword())); // Encrypt if password is being updated
        }

        // Set roles based on the provided roles in UserDto and current user permissions
        Set<Role> roles = userDto.getRoles().stream()
                .map(roleName -> roleRepo.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                .collect(Collectors.toSet());
        existingUser.setRoles(roles);

        // Retain original createdAt and isActive values
        LocalDateTime createdAt = existingUser.getCreatedAt();
        Boolean isActive = existingUser.getIsActive();

        // Configure modelMapper to skip the `id`, `createdAt`, and `isActive` fields mapping
        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper -> {
            mapper.skip(User::setId);
            mapper.skip(User::setCreatedAt);
            mapper.skip(User::setIsActive);
            mapper.skip(User::setPassword);
        });

        // Map remaining fields from userDto to existingUser, excluding `id`, `createdAt`, and `isActive`
        modelMapper.map(userDto, existingUser);

        // Reapply the preserved values
        existingUser.setCreatedAt(createdAt);
        existingUser.setIsActive(isActive);

        // Save the updated user to the repository
        existingUser = userRepository.save(existingUser);
        logger.info("Updated user: {}", existingUser.getUsername());
        return modelMapper.map(existingUser, UserDto.class);
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
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
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
