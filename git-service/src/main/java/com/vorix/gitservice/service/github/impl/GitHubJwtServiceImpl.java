package com.vorix.gitservice.service.github.impl;

import com.vorix.gitservice.config.GitHubAppProperties;
import com.vorix.gitservice.security.PrivateKeyLoader;
import com.vorix.gitservice.service.github.GitHubJwtService;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubJwtServiceImpl implements GitHubJwtService {

    private final PrivateKeyLoader privateKeyLoader;
    private final GitHubAppProperties properties;

    @Override
    public String generateJwt() {

        PrivateKey privateKey = privateKeyLoader.loadPrivateKey();

        Instant now = Instant.now();

        String jwt = Jwts.builder()
                .issuer(properties.getAppId().toString())
                .issuedAt(Date.from(now.minusSeconds(60)))
                .expiration(Date.from(now.plusSeconds(540)))
                .signWith(privateKey)
                .compact();

        log.debug("Generated GitHub App JWT.");

        return jwt;
    }
}