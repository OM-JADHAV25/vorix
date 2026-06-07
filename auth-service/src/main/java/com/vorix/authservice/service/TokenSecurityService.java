package com.vorix.authservice.service;

import java.util.UUID;

public interface TokenSecurityService {

    void revokeAllActiveRefreshTokens(UUID userId);
}
