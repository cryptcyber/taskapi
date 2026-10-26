package com.dylej.taskapi.service;

import com.dylej.taskapi.dto.RegisterRequest;
import com.dylej.taskapi.entity.AppUser;
import com.dylej.taskapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}