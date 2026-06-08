package com.vorix.authservice.service;

import com.vorix.authservice.dto.request.LoginRequest;
import com.vorix.authservice.dto.request.LogoutRequest;
import com.vorix.authservice.dto.request.RefreshTokenRequest;
import com.vorix.authservice.dto.request.RegisterRequest;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RefreshTokenResponse;
import com.vorix.authservice.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void verifyEmail(String token);
}
