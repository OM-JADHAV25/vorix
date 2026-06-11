package com.vorix.authservice.service;

import com.vorix.authservice.dto.oauth.GitHubUserInfo;
import com.vorix.authservice.entity.User;

public interface GitHubUserProvisioningService {

    User createGitHubUser(GitHubUserInfo gitHubUser);
}
