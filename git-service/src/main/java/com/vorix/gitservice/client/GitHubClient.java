package com.vorix.gitservice.client;

public interface GitHubClient {

    <T> T get(
            String uri,
            String accessToken,
            Class<T> responseType
    );

    <T, R> R post(
            String uri,
            String accessToken,
            T request,
            Class<R> responseType
    );

    <R> R postWithJwt(
            String uri,
            String jwt,
            Class<R> responseType
    );
}