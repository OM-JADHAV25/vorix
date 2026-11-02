package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.PullRequestReviewAction;
import com.vorix.gitservice.dto.github.webhook.PullRequestReviewWebhookPayload;
import com.vorix.gitservice.processor.PullRequestReviewProcessor;
import com.vorix.gitservice.service.github.pullrequest.PullRequestReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullRequestReviewProcessorImpl implements PullRequestReviewProcessor {

    private final ObjectMapper objectMapper;
    private final PullRequestReviewService pullRequestReviewService;

    @Override
    public void process(String webhookPayload, String action) {

        try {

            PullRequestReviewWebhookPayload payload = objectMapper.readValue(webhookPayload, PullRequestReviewWebhookPayload.class);

            PullRequestReviewAction reviewAction = PullRequestReviewAction.valueOf(action.toUpperCase());

            switch (reviewAction) {

                case SUBMITTED -> pullRequestReviewService.handleSubmitted(payload);

                case EDITED -> pullRequestReviewService.handleEdited(payload);

                case DISMISSED -> pullRequestReviewService.handleDismissed(payload);
            }

        } catch (Exception ex) {

            log.error("Failed to process pull_request_review webhook.", ex);

            throw new RuntimeException(ex);
        }
    }
}