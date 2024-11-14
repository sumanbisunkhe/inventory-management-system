package com.example.inventory.controller;

import com.example.inventory.dto.UserDto;
import com.example.inventory.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Register a new user
     * @param userDto User details for registration
     * @param bindingResult Validation errors if any
     * @return Confirmation message and user details
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Collect validation errors and return them as a JSON response with a "message" field
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("message", errors);
            return ResponseEntity.badRequest().body(response);

        }

        UserDto registeredUser = userService.registerUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                String.format("User '%s' registered successfully with ID: %d.", registeredUser.getUsername(), registeredUser.getId()));
    }

    /**
     * Update user details
     * @param userId User ID to be updated
     * @param userDto Updated user details
     * @param bindingResult Validation errors if any
     * @return Confirmation message with updated user details
     */
    @PutMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                        @Valid @RequestBody UserDto userDto,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Collect validation errors and return them
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(
                String.format("User with ID: %d updated successfully. New username: '%s'.", updatedUser.getId(), updatedUser.getUsername()));
    }

    /**
     * Get user details by ID
     * @param userId User ID to fetch details
     * @return User details or error message if not found
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(userDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with ID: %d. Please check the ID and try again.", userId));
        }
    }

    /**
     * Get all users
     * @return List of all users
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Delete user by ID
     * @param userId User ID to delete
     * @return Confirmation message or error if user not found
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(String.format("User with ID: %d deleted successfully.", userId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with ID: %d. Unable to delete non-existent user.", userId));
        }
    }

    /**
     * Activate a user
     * @param userId User ID to activate
     * @return Confirmation message with current status
     */
    @PostMapping("/activate/{userId}")
    public ResponseEntity<?> activateUser(@PathVariable Long userId) {
        try {
            UserDto activatedUser = userService.activateUser(userId);
            return ResponseEntity.ok(
                    String.format("User with ID: %d activated successfully. Current status: '%s'.", activatedUser.getId(), activatedUser.getIsActive() ? "Active" : "Inactive"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with ID: %d. Unable to activate non-existent user.", userId));
        }
    }

    /**
     * Deactivate a user
     * @param userId User ID to deactivate
     * @return Confirmation message with current status
     */
    @PostMapping("/deactivate/{userId}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long userId) {
        try {
            UserDto deactivatedUser = userService.deactivateUser(userId);
            return ResponseEntity.ok(
                    String.format("User with ID: %d deactivated successfully. Current status: '%s'.", deactivatedUser.getId(), deactivatedUser.getIsActive() ? "Active" : "Inactive"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with ID: %d. Unable to deactivate non-existent user.", userId));
        }
    }

    /**
     * Find a user by username
     * @param username Username to search for
     * @return User details or error message if not found
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        try {
            UserDto userDto = userService.findByUsername(username);
            return ResponseEntity.ok(userDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with username: '%s'. Please check the username and try again.", username));
        }
    }

    /**
     * Find a user by email
     * @param email Email to search for
     * @return User details or error message if not found
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email) {
        try {
            UserDto userDto = userService.findByEmail(email);
            return ResponseEntity.ok(userDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    String.format("User not found with email: '%s'. Please check the email and try again.", email));
        }
    }
}
