package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.vorix.gitservice.processor.*;
import com.vorix.gitservice.processor.WebhookDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDispatcherImpl implements WebhookDispatcher {

    private final ObjectMapper objectMapper;

    private final InstallationProcessor installationProcessor;
    private final RepositorySyncProcessor repositorySyncProcessor;
    private final PullRequestProcessor pullRequestProcessor;

    @Override
    public void dispatch(String eventType, String webhookPayload) {

        try {

            JsonNode root = objectMapper.readTree(webhookPayload);

            String action = root.path("action").asText(null);

            log.info("Dispatching GitHub Event. Event={}, Action={}", eventType, action);

            switch (eventType) {

                case "installation" -> installationProcessor.process(webhookPayload, action);

                case "installation_repositories" -> repositorySyncProcessor.process(webhookPayload);

                case "pull_request" -> pullRequestProcessor.process(webhookPayload, action);

                default -> log.info("GitHub Event '{}' is not implemented yet.", eventType);
            }

        } catch (Exception ex) {

            log.error("Failed while dispatching GitHub webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}