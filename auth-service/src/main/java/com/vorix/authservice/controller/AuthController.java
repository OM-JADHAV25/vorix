package com.vorix.authservice.controller;

import com.vorix.authservice.dto.request.LoginRequest;
import com.vorix.authservice.dto.request.RefreshTokenRequest;
import com.vorix.authservice.dto.request.RegisterRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RefreshTokenResponse;
import com.vorix.authservice.dto.response.RegisterResponse;
import com.vorix.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        RefreshTokenResponse response = authService.refreshToken(request);

        return ResponseEntity.ok(response);
    }
}
