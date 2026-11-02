package com.vorix.gitservice.service.github.issues;

import com.vorix.gitservice.dto.github.webhook.IssuesWebhookPayload;

public interface IssuesService {

    void handleOpened(IssuesWebhookPayload payload);

    void handleEdited(IssuesWebhookPayload payload);

    void handleDeleted(IssuesWebhookPayload payload);

    void handleClosed(IssuesWebhookPayload payload);

    void handleReopened(IssuesWebhookPayload payload);

    void handleAssigned(IssuesWebhookPayload payload);

    void handleUnassigned(IssuesWebhookPayload payload);

    void handleLabeled(IssuesWebhookPayload payload);

    void handleUnlabeled(IssuesWebhookPayload payload);

    void handleMilestoned(IssuesWebhookPayload payload);

    void handleDemilestoned(IssuesWebhookPayload payload);

    void handleTransferred(IssuesWebhookPayload payload);

    void handlePinned(IssuesWebhookPayload payload);

    void handleUnpinned(IssuesWebhookPayload payload);

    void handleLocked(IssuesWebhookPayload payload);

    void handleUnlocked(IssuesWebhookPayload payload);
}
