package com.vorix.authservice.security.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github.oauth")
public record GitHubOAuthProperties(
        
        String clientId,
        String clientSecret
) {
}
