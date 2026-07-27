package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.dto.github.GitHubRepositorySyncPayload;
import com.vorix.gitservice.processor.InstallationRepositoriesProcessor;
import com.vorix.gitservice.service.RepositorySyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallationRepositoriesProcessorImpl implements InstallationRepositoriesProcessor {

    private final ObjectMapper objectMapper;
    private final RepositorySyncService repositorySyncService;

    @Override
    public void process(String webhookPayload) {

        try {

            GitHubRepositorySyncPayload payload = objectMapper.readValue(webhookPayload, GitHubRepositorySyncPayload.class);

            repositorySyncService.synchronizeRepositories(payload);

            String action = payload.action();

            log.info("Installation repositories action: {}", action);

        } catch (Exception ex) {

            log.error("Failed to process installation_repositories webhook", ex);

            throw new RuntimeException("Unable to process installation_repositories webhook", ex);
        }
    }
}