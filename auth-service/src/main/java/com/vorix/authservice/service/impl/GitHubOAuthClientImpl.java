package com.vorix.authservice.service.impl;

import com.vorix.authservice.dto.oauth.GitHubUserInfo;
import com.vorix.authservice.dto.oauth.github.AccessTokenRequest;
import com.vorix.authservice.dto.oauth.github.GitHubAccessTokenResponse;
import com.vorix.authservice.dto.oauth.github.GitHubEmailResponse;
import com.vorix.authservice.dto.oauth.github.GitHubUserResponse;
import com.vorix.authservice.exception.AuthenticationException;
import com.vorix.authservice.security.oauth.GitHubOAuthProperties;
import com.vorix.authservice.service.GitHubOAuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubOAuthClientImpl implements GitHubOAuthClient{

    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";

    private static final String USER_URL = "https://api.github.com/user";

    private static final String EMAILS_URL = "https://api.github.com/user/emails";

    private final RestClient restClient;
    private final GitHubOAuthProperties gitHubOAuthProperties;

    @Override
    public GitHubUserInfo getUserInfo(String authorizationCode) {

        String accessToken = exchangeCodeForAccessToken(authorizationCode);

        GitHubUserResponse user = fetchUser(accessToken);

        GitHubEmailResponse primaryEmail = fetchPrimaryVerifiedEmail(accessToken);

        return new GitHubUserInfo(
                String.valueOf(user.id()),
                primaryEmail.email(),
                user.login(),
                primaryEmail.verified()
        );
    }

    private String exchangeCodeForAccessToken(String authorizationCode) {

        try {

            GitHubAccessTokenResponse response = restClient.post()
                                                           .uri(ACCESS_TOKEN_URL)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .accept(MediaType.APPLICATION_JSON)
                                                           .body(
                                                                   new AccessTokenRequest(
                                                                         gitHubOAuthProperties.clientId(),
                                                                         gitHubOAuthProperties.clientSecret(),
                                                                         authorizationCode
                                                                   )
                                                           )
                                                           .retrieve()
                                                           .body(GitHubAccessTokenResponse.class);


            if (response == null || response.accessToken() == null || response.accessToken().isBlank()) {

                throw new AuthenticationException("GitHub access token exchange failed");
            }

            return response.accessToken();

        } catch (RestClientException ex) {

            log.error("Failed to exchange GitHub authorization code", ex);

            throw new AuthenticationException("GitHub authentication failed");
        }
    }

    private GitHubUserResponse fetchUser(String accessToken) {

        try {

            return restClient.get()
                             .uri(USER_URL)
                             .headers(headers -> headers.setBearerAuth(accessToken))
                             .retrieve()
                             .body(GitHubUserResponse.class);

        } catch (RestClientException ex) {

            log.error("Failed to fetch GitHub user", ex);

            throw new AuthenticationException("Failed to retrieve GitHub user information");
        }
    }

    private GitHubEmailResponse fetchPrimaryVerifiedEmail(String accessToken) {

        try {

            GitHubEmailResponse[] emails = restClient.get()
                                                     .uri(EMAILS_URL)
                                                     .headers(headers -> headers.setBearerAuth(accessToken))
                                                     .retrieve()
                                                     .body(GitHubEmailResponse[].class);

            if (emails == null || emails.length == 0) {

                throw new AuthenticationException("GitHub did not return email information");
            }

            return Arrays.stream(emails)
                    .filter(GitHubEmailResponse::primary)
                    .filter(GitHubEmailResponse::verified)
                    .findFirst()
                    .orElseThrow(() -> new AuthenticationException("No primary verified GitHub email found"));

        } catch (RestClientException ex) {

            log.error("Failed to retrieve GitHub email information", ex);

            throw new AuthenticationException("Failed to retrieve GitHub email information");
        }
    }
}
