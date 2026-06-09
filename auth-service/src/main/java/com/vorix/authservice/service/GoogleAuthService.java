package com.vorix.authservice.service;

import com.vorix.authservice.dto.request.GoogleLoginRequest;
import com.vorix.authservice.dto.response.LoginResponse;

public interface GoogleAuthService {

    LoginResponse authenticate(GoogleLoginRequest request);
}