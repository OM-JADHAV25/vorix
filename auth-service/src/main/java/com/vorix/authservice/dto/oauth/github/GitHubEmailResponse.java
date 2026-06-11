package com.vorix.authservice.dto.oauth.github;

public record GitHubEmailResponse(

        String email,
        boolean primary,
        boolean verified
) {
}
