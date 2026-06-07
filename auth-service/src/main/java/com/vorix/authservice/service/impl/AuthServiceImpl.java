package com.vorix.authservice.service.impl;

import com.vorix.authservice.dto.request.LoginRequest;
import com.vorix.authservice.dto.request.RegisterRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RegisterResponse;
import com.vorix.authservice.entity.RefreshToken;
import com.vorix.authservice.entity.Role;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.enums.AuthProvider;
import com.vorix.authservice.enums.RoleName;
import com.vorix.authservice.exception.AuthenticationException;
import com.vorix.authservice.exception.DuplicateResourceException;
import com.vorix.authservice.exception.ResourceNotFoundException;
import com.vorix.authservice.repository.RefreshTokenRepository;
import com.vorix.authservice.repository.RoleRepository;
import com.vorix.authservice.repository.UserRepository;
import com.vorix.authservice.security.jwt.JwtService;
import com.vorix.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException(
                    "Email already exists"
            );
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException(
                    "Username already exists"
            );
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() ->
                        new ResourceNotFoundException("USER role not found"));

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .provider(AuthProvider.LOCAL)
                .emailVerified(false)
                .active(true)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .deleted(false)
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new AuthenticationException("Invalid email or password"));

        if (!user.isActive()) {
            throw new AuthenticationException("Account is inactive");
        }

        if (user.isAccountLocked()) {
            throw new AuthenticationException("Account is locked");
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        )) {

            throw new AuthenticationException("Invalid email or password");
        }

        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());

        String accessToken = jwtService.generateAccessToken(
                        user.getId(),
                        user.getEmail(),
                        roles
                );

        String refreshToken = jwtService.generateRefreshToken(user.getId());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                        .id(UUID.randomUUID())
                        .user(user)
                        .tokenHash(refreshToken)
                        .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                        .revoked(false)
                        .createdAt(Instant.now())
                        .build();

        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(accessToken, refreshToken, "Bearer", 900L);
    }
}
