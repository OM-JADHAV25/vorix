package com.vorix.gitservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "github.app")
public class GitHubAppProperties {

    // GitHub App ID
    private Long appId;


    // GitHub App Client ID
    private String clientId;

    // GitHub App Client Secret
    private String clientSecret;

    // GitHub App Private Key location
    private String privateKeyLocation;

    // GitHub Webhook Secret
    private String webhookSecret;
}
