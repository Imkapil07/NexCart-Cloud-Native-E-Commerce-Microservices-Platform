package com.ecommerce.user.controller;


import com.ecommerce.user.dto.LoginRequestDto;
import com.ecommerce.user.dto.LoginResponseDto;
import com.ecommerce.user.dto.UserCreateRequestDto;
import com.ecommerce.user.dto.UserResponseDto;
import com.ecommerce.user.service.impl.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


/**
 * REST controller for user registration, login,
 * and profile retrieval. Base path: /api/users
 **/
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Registers a new user. Returns 201 Created
     * with the created user (password excluded).
     **/
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserCreateRequestDto dto){
        UserResponseDto created =  userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Authenticates user with email/password and returns
     * JWT token and user info.
     **/
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
        LoginResponseDto resp =  userService.login(dto);
        return ResponseEntity.ok(resp);
    }

    /**
     * Returns user profile by ID. Requires valid JWT
     * for authenticated users.
     **/
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id, Authentication authentication){
        UserResponseDto dto =  userService.getById(id);
        return ResponseEntity.ok(dto);
    }
}