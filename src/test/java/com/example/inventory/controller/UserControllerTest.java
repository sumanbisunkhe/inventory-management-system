package com.example.inventory.controller;

import com.example.inventory.dto.UserDto;
import com.example.inventory.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        // Initialize ObjectMapper manually before registering modules
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Set up a sample userDto
        userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .fullName("Test User")
                .roles(Set.of("CUSTOMER"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .isActive(true)
                .build();
    }


    @Test
    void testRegisterUser_Success() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .password("strongpassword123")
                .email("test@example.com")
                .fullName("Test User")
                .roles(Set.of("CUSTOMER"))
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .phoneNumber("+1234567890")
                .build();

        when(userService.registerUser(any(UserDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())  // Expecting 201 Created status
                .andExpect(content().string("User 'testuser' registered successfully with ID: 1."));
    }


    @Test
    void testRegisterUser_ValidationErrors() throws Exception {
        UserDto invalidUserDto = UserDto.builder().username("").email("test@example").build();

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isArray())  // Ensure the "message" field is an array
                .andExpect(jsonPath("$.message", hasItem("password: Password is required")))
                .andExpect(jsonPath("$.message", hasItem("roles: Roles are required")))
                .andExpect(jsonPath("$.message", hasItem("username: Username must be between 3 and 20 characters")))
                .andExpect(jsonPath("$.message", hasItem("fullName: Full name is required")))
                .andExpect(jsonPath("$.message", hasItem("username: Username is required")));
    }





    @Test
    void testUpdateUser_Success() throws Exception {
        // Create a valid UserDto object with all necessary fields
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("testuser");
        userDto.setFullName("Test User");
        userDto.setEmail("testuser@example.com");
        userDto.setPassword("validPassword123");
        userDto.setRoles(Collections.singleton("CUSTOMER"));
        userDto.setDateOfBirth(LocalDate.of(1990, 1, 1)); // A valid past date
        userDto.setPhoneNumber("+1234567890");
        userDto.setAddress("123 Test Street");

        // Mock the response from the userService
        when(userService.updateUser(eq(1L), any(UserDto.class))).thenReturn(userDto);

        // Perform the PUT request to update the user
        mockMvc.perform(put("/api/users/update/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID: 1 updated successfully. New username: 'testuser'."));
    }


    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userDto);

        mockMvc.perform(get("/api/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenThrow(new EntityNotFoundException("User not found with id: 1"));

        mockMvc.perform(get("/api/users/{userId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with ID: 1. Please check the ID and try again."));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        UserDto userDto2 = UserDto.builder().id(2L).username("user2").build();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(userDto, userDto2));

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/delete/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID: 1 deleted successfully."));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        doThrow(new EntityNotFoundException("User not found with id: 1")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/delete/{userId}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with ID: 1. Unable to delete non-existent user."));
    }

    @Test
    void testActivateUser_Success() throws Exception {
        when(userService.activateUser(1L)).thenReturn(userDto);

        mockMvc.perform(post("/api/users/activate/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID: 1 activated successfully. Current status: 'Active'."));
    }

    @Test
    void testDeactivateUser_Success() throws Exception {
        when(userService.deactivateUser(1L)).thenReturn(userDto);

        mockMvc.perform(post("/api/users/deactivate/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("User with ID: 1 deactivated successfully. Current status: 'Active'."));
    }

    @Test
    void testFindByUsername_Success() throws Exception {
        when(userService.findByUsername("testuser")).thenReturn(userDto);

        mockMvc.perform(get("/api/users/username/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testFindByUsername_NotFound() throws Exception {
        when(userService.findByUsername("testuser")).thenThrow(new EntityNotFoundException("User not found with username: testuser"));

        mockMvc.perform(get("/api/users/username/{username}", "testuser"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with username: 'testuser'. Please check the username and try again."));
    }

    @Test
    void testFindByEmail_Success() throws Exception {
        when(userService.findByEmail("testuser@example.com")).thenReturn(userDto);

        mockMvc.perform(get("/api/users/email/{email}", "testuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    void testFindByEmail_NotFound() throws Exception {
        when(userService.findByEmail("testuser@example.com")).thenThrow(new EntityNotFoundException("User not found with email: testuser@example.com"));

        mockMvc.perform(get("/api/users/email/{email}", "testuser@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found with email: 'testuser@example.com'. Please check the email and try again."));
    }
}
