package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.vorix.gitservice.dto.github.GitHubInstallationPayload;
import com.vorix.gitservice.processor.InstallationProcessor;
import com.vorix.gitservice.service.GitHubInstallationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallationProcessorImpl implements InstallationProcessor {

    private final ObjectMapper objectMapper;

    private final GitHubInstallationService installationService;

    @Override
    public void process(String webhookPayload, String action) {

        try {

            JsonNode root = objectMapper.readTree(webhookPayload);

            GitHubInstallationPayload payload = new GitHubInstallationPayload(
                            root.path("installation")
                                    .path("id")
                                    .asLong(),

                            root.path("installation")
                                    .path("account")
                                    .path("id")
                                    .asLong(),

                            root.path("installation")
                                    .path("account")
                                    .path("login")
                                    .asText(),

                            root.path("installation")
                                    .path("account")
                                    .path("type")
                                    .asText(),

                            root.path("installation")
                                    .path("target_type")
                                    .asText()
                    );

            switch (action) {

                case "created" -> installationService.createInstallation(payload);

                case "deleted" -> installationService.deleteInstallation(payload.installationId());

                case "suspend" -> installationService.suspendInstallation(payload.installationId());

                case "unsuspend" -> installationService.activateInstallation(payload.installationId());

                default -> log.info("Ignoring installation action: {}", action);
            }

        } catch (Exception ex) {

            log.error("Failed to process installation webhook.", ex);
            throw new RuntimeException(ex);
        }

    }
}