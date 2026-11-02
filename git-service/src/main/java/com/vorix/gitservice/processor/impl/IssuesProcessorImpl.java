
package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.IssueAction;
import com.vorix.gitservice.dto.github.webhook.IssuesWebhookPayload;
import com.vorix.gitservice.processor.IssuesProcessor;
import com.vorix.gitservice.service.github.issues.IssuesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class IssuesProcessorImpl implements IssuesProcessor {

    private final ObjectMapper objectMapper;
    private final IssuesService issuesService;

    @Override
    public void process(String webhookPayload, String action) {

        if (webhookPayload == null || webhookPayload.isBlank()) {
            throw new IllegalArgumentException("Issues webhook payload must not be empty");
        }

        try {
            IssuesWebhookPayload payload = objectMapper.readValue(webhookPayload, IssuesWebhookPayload.class);

            if (payload == null) {
                throw new IllegalArgumentException("Issues webhook payload could not be parsed");
            }

            String resolvedAction = (action != null && !action.isBlank()) ? action : payload.action();

            if (resolvedAction == null || resolvedAction.isBlank()) {
                log.warn("Ignoring issues webhook because action is missing");
                return;
            }

            IssueAction issueAction;

            try {
                issueAction = IssueAction.valueOf(resolvedAction.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                log.info("Ignoring unsupported GitHub issue action: {}", resolvedAction);
                return;
            }

            switch (issueAction) {
                case OPENED -> issuesService.handleOpened(payload);
                case EDITED -> issuesService.handleEdited(payload);
                case DELETED -> issuesService.handleDeleted(payload);
                case CLOSED -> issuesService.handleClosed(payload);
                case REOPENED -> issuesService.handleReopened(payload);
                case ASSIGNED -> issuesService.handleAssigned(payload);
                case UNASSIGNED -> issuesService.handleUnassigned(payload);
                case LABELED -> issuesService.handleLabeled(payload);
                case UNLABELED -> issuesService.handleUnlabeled(payload);
                case MILESTONED -> issuesService.handleMilestoned(payload);
                case DEMILESTONED -> issuesService.handleDemilestoned(payload);
                case TRANSFERRED -> issuesService.handleTransferred(payload);
                case PINNED -> issuesService.handlePinned(payload);
                case UNPINNED -> issuesService.handleUnpinned(payload);
                case LOCKED -> issuesService.handleLocked(payload);
                case UNLOCKED -> issuesService.handleUnlocked(payload);
            }

            log.debug("Successfully processed GitHub issues webhook. Action={}", issueAction);

        } catch (Exception ex) {
            log.error("Failed to process GitHub issues webhook. Action={}", action, ex);

            throw new IllegalStateException("Failed to process GitHub issues webhook", ex);
        }
    }
}
