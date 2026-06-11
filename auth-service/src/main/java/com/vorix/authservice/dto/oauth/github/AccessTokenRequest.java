package com.vorix.authservice.dto.oauth.github;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenRequest(

        @JsonProperty("client_id")
        String clientId,

        @JsonProperty("client_secret")
        String clientSecret,

        String code
) {
}
