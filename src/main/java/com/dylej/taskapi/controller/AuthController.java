package com.dylej.taskapi.controller;

import com.dylej.taskapi.dto.LoginRequest;
import com.dylej.taskapi.dto.LoginResponse;
import com.dylej.taskapi.dto.RegisterRequest;
import com.dylej.taskapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

                System.out.println(">>> LOGIN ENDPOINT HIT <<<");
                
        return authService.login(request);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Valid @RequestBody RegisterRequest request) {
        System.out.println(">>> REGISTER ENDPOINT HIT <<<");
        authService.register(request);
    }

     @GetMapping("/test")
    public String test() {
        return "Auth controller works";
    }
}