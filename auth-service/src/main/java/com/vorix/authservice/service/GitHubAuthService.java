package com.vorix.authservice.service;

import com.vorix.authservice.dto.request.GitHubLoginRequest;
import com.vorix.authservice.dto.response.LoginResponse;

public interface GitHubAuthService {

    LoginResponse authenticate(GitHubLoginRequest request);

}
