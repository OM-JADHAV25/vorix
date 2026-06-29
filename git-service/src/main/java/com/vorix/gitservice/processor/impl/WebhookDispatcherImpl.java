package com.vorix.gitservice.processor.impl;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import com.vorix.gitservice.processor.InstallationProcessor;
import com.vorix.gitservice.processor.WebhookDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDispatcherImpl implements WebhookDispatcher {

    private final InstallationProcessor installationProcessor;
    private final ObjectMapper objectMapper;

    @Override
    public void dispatch(String eventType, String webhookPayload) {

        try {

            JsonNode root = objectMapper.readTree(webhookPayload);

            String action = root.path("action").asText(null);

            switch (eventType) {

                case "installation" -> installationProcessor.process(webhookPayload, action);

                default -> log.info("Ignoring unsupported GitHub event '{}'", eventType);
            }

        } catch (Exception ex) {

            log.error("Failed to dispatch GitHub webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}