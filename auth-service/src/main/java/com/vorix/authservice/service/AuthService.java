package com.vorix.authservice.service;

import com.vorix.authservice.dto.request.*;
import com.vorix.authservice.dto.response.LoginResponse;
import com.vorix.authservice.dto.response.RefreshTokenResponse;
import com.vorix.authservice.dto.response.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);

    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);

    void verifyEmail(String token);

    void forgotPassword(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
