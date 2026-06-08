package com.vorix.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(

        long passwordResetTokenExpiration,
        long emailVerificationTokenExpiration
) {
}