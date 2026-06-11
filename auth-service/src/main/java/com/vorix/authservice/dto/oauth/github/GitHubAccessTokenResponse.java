package com.vorix.authservice.dto.oauth.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubAccessTokenResponse(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("token_type")
        String tokenType,

        String scope
) {
}
