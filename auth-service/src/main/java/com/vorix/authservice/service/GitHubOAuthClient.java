package com.vorix.authservice.service;

import com.vorix.authservice.dto.oauth.GitHubUserInfo;

public interface GitHubOAuthClient {

    GitHubUserInfo getUserInfo(String authorizationCode);
}
