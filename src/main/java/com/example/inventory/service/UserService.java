package com.example.inventory.service;


import com.example.inventory.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserDto userDto);

    UserDto updateUser(Long userId, UserDto userDto);

    UserDto getUserById(Long userId);

    List<UserDto> getAllUsers();

    void deleteUser(Long userId);

    UserDto activateUser(Long userId);

    UserDto deactivateUser(Long userId);

    UserDto findByUsername(String username);

    UserDto findByEmail(String email);
}
