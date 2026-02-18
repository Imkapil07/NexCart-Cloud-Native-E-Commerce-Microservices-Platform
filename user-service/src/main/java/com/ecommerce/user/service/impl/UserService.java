package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.LoginRequestDto;
import com.ecommerce.user.dto.LoginResponseDto;
import com.ecommerce.user.dto.UserCreateRequestDto;
import com.ecommerce.user.dto.UserResponseDto;

/**
 * Service contract for user operations: registration,
 * login, and fetching user by ID.
 **/
public interface UserService {
    /**
     * Register a new user; throws if email already exists.
     **/
    UserResponseDto register(UserCreateRequestDto dto);
    /**
     * Authenticate and return JWT + user info;
     * throws on invalid credentials.
     **/
    LoginResponseDto login(LoginRequestDto dto);
    /**
     * Get user by ID; throws ResourceNotFoundException if not found.
     **/
    UserResponseDto getById(Long id);
}