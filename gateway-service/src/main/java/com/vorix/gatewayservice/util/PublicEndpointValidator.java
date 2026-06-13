package com.vorix.gatewayservice.util;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicEndpointValidator {

    private static final List<String> PUBLIC_ENDPOINTS = List.of(

            "/api/v1/auth/register",
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",

            "/api/v1/auth/oauth/google",
            "/api/v1/auth/oauth/github",

            "/api/v1/auth/forgot-password",
            "/api/v1/auth/reset-password",

            "/api/v1/auth/verify-email"
    );

    public boolean isPublic(String path) {

        return PUBLIC_ENDPOINTS.stream().anyMatch(path::startsWith);
    }
}