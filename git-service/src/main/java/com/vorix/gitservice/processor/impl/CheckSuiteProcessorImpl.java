package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.CheckSuiteAction;
import com.vorix.gitservice.dto.github.webhook.CheckSuiteWebhookPayload;
import com.vorix.gitservice.processor.CheckSuiteProcessor;
import com.vorix.gitservice.service.github.checks.CheckSuiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckSuiteProcessorImpl implements CheckSuiteProcessor {

    private final ObjectMapper objectMapper;
    private final CheckSuiteService checkSuiteService;

    @Override
    public void process(String webhookPayload, String action) {

        try {

            CheckSuiteWebhookPayload payload = objectMapper.readValue(webhookPayload, CheckSuiteWebhookPayload.class);

            CheckSuiteAction checkSuiteAction = CheckSuiteAction.valueOf(action.toUpperCase());

            switch (checkSuiteAction) {

                case REQUESTED -> checkSuiteService.handleRequested(payload);

                case REREQUESTED -> checkSuiteService.handleRerequested(payload);

                case COMPLETED -> checkSuiteService.handleCompleted(payload);
            }

        } catch (Exception ex) {

            log.error("Failed to process check_suite webhook.", ex);

            throw new RuntimeException(ex);
        }
    }
}