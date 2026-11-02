package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.PullRequestReviewCommentAction;
import com.vorix.gitservice.dto.github.webhook.PullRequestReviewCommentWebhookPayload;
import com.vorix.gitservice.processor.PullRequestReviewCommentProcessor;
import com.vorix.gitservice.service.github.pullrequest.PullRequestReviewCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullRequestReviewCommentProcessorImpl implements PullRequestReviewCommentProcessor {

    private final ObjectMapper objectMapper;
    private final PullRequestReviewCommentService reviewCommentService;

    @Override
    public void process(String webhookPayload, String action) {

        if (webhookPayload == null || webhookPayload.isBlank()) {
            throw new IllegalArgumentException("Pull request review comment webhook payload must not be empty");
        }

        try {
            PullRequestReviewCommentWebhookPayload payload = objectMapper.readValue(webhookPayload, PullRequestReviewCommentWebhookPayload.class);

            String resolvedAction = (action != null && !action.isBlank()) ? action : payload.action();

            if (resolvedAction == null || resolvedAction.isBlank()) {
                log.warn("Review comment webhook received without an action");
                return;
            }

            PullRequestReviewCommentAction commentAction;

            try {commentAction = PullRequestReviewCommentAction.valueOf(resolvedAction.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                log.info("Ignoring unsupported review comment action: {}", resolvedAction);
                return;
            }

            switch (commentAction) {
                case CREATED -> reviewCommentService.handleCreated(payload);

                case EDITED -> reviewCommentService.handleEdited(payload);

                case DELETED -> reviewCommentService.handleDeleted(payload);
            }

            log.debug("Successfully processed review comment webhook. Action={}", commentAction);

        } catch (Exception ex) {
            log.error("Failed to process pull_request_review_comment webhook. Action={}", action, ex);

            throw new IllegalStateException("Failed to process pull request review comment webhook", ex);
        }
    }
}
