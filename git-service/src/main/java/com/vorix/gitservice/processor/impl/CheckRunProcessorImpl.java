package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.dto.github.webhook.CheckRunWebhookPayload;
import com.vorix.gitservice.domain.enums.CheckRunAction;
import com.vorix.gitservice.processor.CheckRunProcessor;
import com.vorix.gitservice.service.github.checks.CheckRunService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckRunProcessorImpl implements CheckRunProcessor {

    private final ObjectMapper objectMapper;
    private final CheckRunService checkRunService;

    @Override
    public void process(String webhookPayload, String action) {

        try {

            CheckRunWebhookPayload payload = objectMapper.readValue(webhookPayload, CheckRunWebhookPayload.class);

            CheckRunAction checkRunAction = CheckRunAction.valueOf(action.toUpperCase());

            switch (checkRunAction) {

                case CREATED -> checkRunService.handleCreated(payload);

                case REREQUESTED -> checkRunService.handleRerequested(payload);

                case COMPLETED -> checkRunService.handleCompleted(payload);

                case REQUESTED_ACTION -> checkRunService.handleRequestedAction(payload);
            }

        } catch (Exception ex) {

            log.error("Failed to process check_run webhook", ex);

            throw new RuntimeException(ex);
        }
    }

    private void handleCreated(CheckRunWebhookPayload payload) {

        log.info(
                "Check Run {} created for repository {}",
                payload.checkRun().id(),
                payload.repository().fullName()
        );
    }

    private void handleRerequested(CheckRunWebhookPayload payload) {

        log.info("Check Run {} rerequested", payload.checkRun().id());
    }

    private void handleCompleted(CheckRunWebhookPayload payload) {

        log.info(
                "Check Run {} completed with conclusion {}",
                payload.checkRun().id(),
                payload.checkRun().conclusion()
        );
    }

    private void handleRequestedAction(CheckRunWebhookPayload payload) {

        log.info("Requested action received for Check Run {}", payload.checkRun().id());
    }
}