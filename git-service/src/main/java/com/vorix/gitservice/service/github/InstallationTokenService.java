package com.vorix.gitservice.service.github;

public interface InstallationTokenService {

    String getAccessToken(Long installationId);
}