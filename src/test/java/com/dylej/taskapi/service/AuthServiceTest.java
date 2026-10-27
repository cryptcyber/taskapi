package com.dylej.taskapi.service;

import com.dylej.taskapi.dto.LoginRequest;
import com.dylej.taskapi.dto.LoginResponse;
import com.dylej.taskapi.entity.AppUser;
import com.dylej.taskapi.repository.UserRepository;
import com.dylej.taskapi.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() {
        AppUser user = AppUser.builder()
                .username("test-user")
                .password("encoded-password")
                .build();
        when(userRepository.findByUsername("test-user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded-password"))
                .thenReturn(true);
        when(jwtService.generateToken("test-user")).thenReturn("jwt-token");

        LoginResponse response = authService.login(
                new LoginRequest("test-user", "password"));

        assertEquals("jwt-token", response.token());
        verify(jwtService).generateToken("test-user");
    }

    @Test
    void loginRejectsUnknownUsername() {
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class,
                () -> authService.login(new LoginRequest("missing", "password")));
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void loginRejectsIncorrectPassword() {
        AppUser user = AppUser.builder()
                .username("test-user")
                .password("encoded-password")
                .build();
        when(userRepository.findByUsername("test-user"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password"))
                .thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> authService.login(
                        new LoginRequest("test-user", "wrong-password")));
        verifyNoInteractions(jwtService);
    }
}
