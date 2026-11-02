package com.vorix.gitservice.service.github.issues.impl;

import com.vorix.gitservice.dto.github.webhook.IssuesWebhookPayload;
import com.vorix.gitservice.service.github.issues.IssuesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class IssuesServiceImpl implements IssuesService {

    @Override
    public void handleOpened(IssuesWebhookPayload payload) {
        logIssueEvent("opened", payload);
    }

    @Override
    public void handleEdited(IssuesWebhookPayload payload) {
        logIssueEvent("edited", payload);
    }

    @Override
    public void handleDeleted(IssuesWebhookPayload payload) {
        logIssueEvent("deleted", payload);
    }

    @Override
    public void handleClosed(IssuesWebhookPayload payload) {
        logIssueEvent("closed", payload);
    }

    @Override
    public void handleReopened(IssuesWebhookPayload payload) {
        logIssueEvent("reopened", payload);
    }

    @Override
    public void handleAssigned(IssuesWebhookPayload payload) {
        logIssueEvent("assigned", payload);
    }

    @Override
    public void handleUnassigned(IssuesWebhookPayload payload) {
        logIssueEvent("unassigned", payload);
    }

    @Override
    public void handleLabeled(IssuesWebhookPayload payload) {
        logIssueEvent("labeled", payload);
    }

    @Override
    public void handleUnlabeled(IssuesWebhookPayload payload) {
        logIssueEvent("unlabeled", payload);
    }

    @Override
    public void handleMilestoned(IssuesWebhookPayload payload) {
        logIssueEvent("milestoned", payload);
    }

    @Override
    public void handleDemilestoned(IssuesWebhookPayload payload) {
        logIssueEvent("demilestoned", payload);
    }

    @Override
    public void handleTransferred(IssuesWebhookPayload payload) {
        logIssueEvent("transferred", payload);
    }

    @Override
    public void handlePinned(IssuesWebhookPayload payload) {
        logIssueEvent("pinned", payload);
    }

    @Override
    public void handleUnpinned(IssuesWebhookPayload payload) {
        logIssueEvent("unpinned", payload);
    }

    @Override
    public void handleLocked(IssuesWebhookPayload payload) {
        logIssueEvent("locked", payload);
    }

    @Override
    public void handleUnlocked(IssuesWebhookPayload payload) {
        logIssueEvent("unlocked", payload);
    }

    private void logIssueEvent(
            String eventAction,
            IssuesWebhookPayload payload
    ) {
        if (payload == null || payload.issue() == null || payload.repository() == null) {
            throw new IllegalArgumentException("Issue and repository data are required");
        }

        if (payload.issue().id() == null || payload.issue().number() == null) {
            throw new IllegalArgumentException("Issue ID and issue number are required");
        }

        String repository = payload.repository().fullName();

        if (!StringUtils.hasText(repository)) {
            throw new IllegalArgumentException("Repository full name is required");
        }

        log.info(
                "GitHub issue {}. Repository={}, IssueId={}, IssueNumber={}, Title={}, State={}, Actor={}",
                eventAction,
                repository,
                payload.issue().id(),
                payload.issue().number(),
                payload.issue().title(),
                payload.issue().state(),
                payload.sender() != null ? payload.sender().login() : "unknown"
        );
    }
}
