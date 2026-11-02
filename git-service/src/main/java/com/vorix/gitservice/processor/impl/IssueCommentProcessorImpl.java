package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.IssueCommentAction;
import com.vorix.gitservice.dto.github.webhook.IssueCommentWebhookPayload;
import com.vorix.gitservice.processor.IssueCommentProcessor;
import com.vorix.gitservice.service.github.issues.IssueCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssueCommentProcessorImpl implements IssueCommentProcessor {

    private final ObjectMapper objectMapper;
    private final IssueCommentService issueCommentService;

    @Override
    public void process(String webhookPayload, String action) {

        if (webhookPayload == null || webhookPayload.isBlank()) {
            throw new IllegalArgumentException("Webhook payload cannot be empty.");
        }

        try {
            IssueCommentWebhookPayload payload = objectMapper.readValue(webhookPayload, IssueCommentWebhookPayload.class);

            String resolvedAction = (action != null && !action.isBlank()) ? action : payload.action();

            if (resolvedAction == null || resolvedAction.isBlank()) {
                log.warn("Ignoring issue_comment webhook with missing action.");
                return;
            }

            IssueCommentAction commentAction;

            try {
                commentAction = IssueCommentAction.valueOf(resolvedAction.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                log.info("Ignoring unsupported issue_comment action: {}", resolvedAction);
                return;
            }

            switch (commentAction) {
                case CREATED -> issueCommentService.handleCreated(payload);
                case EDITED -> issueCommentService.handleEdited(payload);
                case DELETED -> issueCommentService.handleDeleted(payload);
            }

            log.debug("Successfully processed issue_comment action: {}", commentAction);

        } catch (Exception ex) {
            log.error("Failed to process issue_comment webhook. Action: {}", action, ex);
            throw new IllegalStateException("Failed to process GitHub issue comment webhook.", ex);
        }
    }
}