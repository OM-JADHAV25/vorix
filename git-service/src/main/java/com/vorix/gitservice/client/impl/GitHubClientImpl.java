package com.vorix.gitservice.client.impl;

import com.vorix.gitservice.client.GitHubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class GitHubClientImpl implements GitHubClient {

    private final RestClient gitHubRestClient;

    @Override
    public <T> T get(
            String uri,
            String accessToken,
            Class<T> responseType
    ) {

        return gitHubRestClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(responseType);
    }

    @Override
    public <T, R> R post(
            String uri,
            String accessToken,
            T request,
            Class<R> responseType
    ) {

        return gitHubRestClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .body(request)
                .retrieve()
                .body(responseType);
    }

    @Override
    public <R> R postWithJwt(
            String uri,
            String jwt,
            Class<R> responseType
    ) {

        return gitHubRestClient.post()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .retrieve()
                .body(responseType);
    }
}