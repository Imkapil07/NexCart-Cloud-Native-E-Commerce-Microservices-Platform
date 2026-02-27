package com.ecommerce.user.service.impl;

import com.ecommerce.user.dto.LoginRequestDto;
import com.ecommerce.user.dto.LoginResponseDto;
import com.ecommerce.user.dto.UserCreateRequestDto;
import com.ecommerce.user.dto.UserResponseDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.exception.ResourceAlreadyExistsException;
import com.ecommerce.user.exception.ResourceNotFoundException;
import com.ecommerce.user.mapper.AuthMapper;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repository.UserRepository;
import com.ecommerce.user.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserService}: user registration,
 * login, and get-by-ID. Passwords are encoded via PasswordEncoder;
 * JWT is generated on successful login.
 **/
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepo;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthMapper authMapper;

    @Override
    public UserResponseDto register(UserCreateRequestDto dto) {
        log.info("[UserService] Register attempt: email={}", dto.getEmail());
        if (userRepo.existsByEmail(dto.getEmail())) {
            log.warn("[UserService] Register failed: email already exists");
            throw new ResourceAlreadyExistsException("Email already registered");
        }
        User user = userMapper.toEntity(dto);
        User saved = userRepo.save(user);
        log.info("[UserService] Register OK: userId={} email={}", saved.getId(), saved.getEmail());
        return userMapper.toDto(saved);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        log.info("[UserService] Login attempt: email={}", dto.getEmail());
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("invalid credential"));
        boolean matches = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if (!matches) {
            log.warn("[UserService] Login failed: invalid password for email={}", dto.getEmail());
            throw new ResourceNotFoundException("invalid credentials");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRoles());
        log.info("[UserService] Login OK: userId={} email={} (use this token as Bearer for protected APIs)", user.getId(), user.getEmail());
        return authMapper.toLoginResponse(user, token);
    }

    @Override
    public UserResponseDto getById(Long id) {
        log.info("[UserService] getById: id={}", id);
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.toDto(user);
    }

}