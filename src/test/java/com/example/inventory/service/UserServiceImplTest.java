package com.example.inventory.service;

import com.example.inventory.dto.UserDto;
import com.example.inventory.model.Role;
import com.example.inventory.model.User;
import com.example.inventory.repo.RoleRepo;
import com.example.inventory.repo.UserRepo;
import com.example.inventory.service.impl.UserServiceImpl;
import com.example.inventory.utils.CustomEmailMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Mock
    private UserRepo userRepository;

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDto userDto;
    private Role customerRole;

    @BeforeEach
    void setUp() {
        // Initialize sample User and UserDto objects
        customerRole = new Role();
        customerRole.setId(1L);
        customerRole.setName("CUSTOMER");

        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .fullName("Test User")
                .roles(Set.of(customerRole))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .username("testuser")
                .email("testuser@example.com")
                .fullName("Test User")
                .roles(Set.of("CUSTOMER"))
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        // Mock authentication context
        Authentication auth = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testRegisterUser_AdminRoleAssignment() {
        // Setup: Admin creates a new user
        when(roleRepo.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(passwordEncoder.encode(anyString())).thenReturn("encryptedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        doNothing().when(emailService).sendEmail(any(CustomEmailMessage.class));

        // Act
        UserDto result = userService.registerUser(userDto);

        // Assert
        assertEquals(userDto.getUsername(), result.getUsername());
        assertEquals(userDto.getEmail(), result.getEmail());
        assertEquals(userDto.getRoles(), result.getRoles());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(any(CustomEmailMessage.class));
    }

    @Test
    void testRegisterUser_NonAdminRoleRestriction() {
        // Setup non-admin trying to assign an admin role
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.empty());
        userDto.setRoles(Set.of("ADMIN"));

        // Assert AccessDeniedException
        assertThrows(AccessDeniedException.class, () -> userService.registerUser(userDto));
    }

    @Test
    void testUpdateUser_ValidUserUpdate() {
        // Setup: Create an existing user with CUSTOMER role
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("olduser");
        existingUser.setEmail("olduser@example.com");
        existingUser.setFullName("Old User");
        existingUser.setPassword("oldPassword");
        existingUser.setIsActive(true);

        Role customerRole = new Role();
        customerRole.setId(2L);
        customerRole.setName("CUSTOMER");

        existingUser.setRoles(Set.of(customerRole));

        // Setup UserDto with updated information
        UserDto updateUserDto = new UserDto();
        updateUserDto.setEmail("newuser@example.com");
        updateUserDto.setFullName("New User");
        updateUserDto.setPassword("newPassword");
        updateUserDto.setRoles(Set.of("CUSTOMER"));

        // Mock authentication for non-admin user
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "customerUser",
                        null,
                        Set.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                )
        );

        // Mock repository and service interactions
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncryptedPassword");

        // Configure the mapping to properly translate DTO to User and User back to DTO
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail(updateUserDto.getEmail());
        updatedUser.setFullName(updateUserDto.getFullName());
        updatedUser.setPassword("newEncryptedPassword");
        updatedUser.setRoles(Set.of(customerRole));

        when(modelMapper.map(updateUserDto, User.class)).thenReturn(updatedUser);
        when(modelMapper.map(updatedUser, UserDto.class)).thenReturn(updateUserDto);
        when(roleRepo.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Act
        UserDto result = userService.updateUser(existingUser.getId(), updateUserDto);

        // Assert
        assertNotNull(result, "The result should not be null");
        assertEquals(updateUserDto.getFullName(), result.getFullName(), "Full name should match updated value");
        assertEquals(updateUserDto.getEmail(), result.getEmail(), "Email should match updated value");
        assertTrue(result.getRoles().contains("CUSTOMER"), "User should retain CUSTOMER role");
        verify(userRepository, times(1)).save(existingUser);

        // Clean up the security context
        SecurityContextHolder.clearContext();
    }


    @Test
    void testUpdateUser_WithNonAdminUserAssigningAdminRole() {
        // Create a user who will be updated
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPassword("oldPassword");
        user.setCreatedAt(LocalDateTime.now());
        user.setIsActive(true);

        // Create role entities
        Role adminRole = new Role();
        adminRole.setId(1L);
        adminRole.setName("ADMIN");

        Role customerRole = new Role();
        customerRole.setId(2L);
        customerRole.setName("CUSTOMER");

        // Assign roles to the user
        user.setRoles(Set.of(adminRole, customerRole));

        // Create UserDto for update
        UserDto userDto = new UserDto();
        userDto.setEmail("updated@example.com");
        userDto.setFullName("Updated User");
        userDto.setPassword("newPassword");
        userDto.setRoles(Set.of("ADMIN", "CUSTOMER"));

        // Create an authenticated user with only the CUSTOMER role (no ADMIN role)
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "customerUser",
                        null,
                        Set.of(new SimpleGrantedAuthority("ROLE_CUSTOMER"))
                )
        );

        // Mock repository to return the user for the given ID
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncryptedPassword");
        when(modelMapper.map(userDto, User.class)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);
        when(roleRepo.findByName("ADMIN")).thenReturn(Optional.of(adminRole));
        when(roleRepo.findByName("CUSTOMER")).thenReturn(Optional.of(customerRole));

        // Act & Assert: Attempt to update user with non-admin role should throw AccessDeniedException
        assertThrows(AccessDeniedException.class, () -> {
            userService.updateUser(user.getId(), userDto);
        });

        // Clear the SecurityContext to avoid side effects on other tests
        SecurityContextHolder.clearContext();
    }


    @Test
    void testGetUserById_UserExists() {
        // Setup
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto result = userService.getUserById(user.getId());

        // Assert
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getUsername(), result.getUsername());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testGetAllUsers() {
        // Setup
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        // Act
        List<UserDto> users = userService.getAllUsers();

        // Assert
        assertEquals(1, users.size());
        assertEquals(userDto.getUsername(), users.get(0).getUsername());
    }

    @Test
    void testDeleteUser_UserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(999L));
    }

    @Test
    void testActivateUser_UserExists() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.activateUser(user.getId());

        assertTrue(result.getIsActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeactivateUser_UserExists() {
        // Set up
        user.setIsActive(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(modelMapper.map(user, UserDto.class)).thenAnswer(invocation -> {
            UserDto dto = new UserDto();
            dto.setIsActive(user.getIsActive()); // Reflects the actual isActive state of user
            return dto;
        });

        // Act
        UserDto result = userService.deactivateUser(user.getId());

        // Assert that the user's active status is now false
        assertFalse(result.getIsActive(), "User should be deactivated");
        verify(userRepository, times(1)).save(user);
    }



    @Test
    void testFindByUsername_UserExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.findByUsername(user.getUsername());

        assertEquals(userDto.getUsername(), result.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername("unknown"));
    }

    @Test
    void testFindByEmail_UserExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

        UserDto result = userService.findByEmail(user.getEmail());

        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void testFindByEmail_UserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.findByEmail("unknown@example.com"));
    }
}
