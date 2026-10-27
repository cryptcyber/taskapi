package com.dylej.taskapi.service;

import com.dylej.taskapi.dto.LoginRequest;
import com.dylej.taskapi.dto.LoginResponse;
import com.dylej.taskapi.dto.RegisterRequest;
import com.dylej.taskapi.entity.AppUser;
import com.dylej.taskapi.repository.UserRepository;
import com.dylej.taskapi.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(
                request.username())) {

            throw new RuntimeException(
                    "Username already exists");
        }

AppUser user = AppUser.builder()
        .username(request.username())
        .password(passwordEncoder.encode(request.password()))
        .role("ROLE_USER")
        .build();

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadCredentialsException(
                        "Invalid username or password"));

        System.out.println("Username: " + request.username());
        System.out.println("Password: " + request.password());
        System.out.println("Encoded Password: " + user.getPassword());
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new LoginResponse(jwtService.generateToken(user.getUsername()));
    }
}