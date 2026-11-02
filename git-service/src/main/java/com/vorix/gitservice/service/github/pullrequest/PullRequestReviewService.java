package com.vorix.gitservice.service.github.pullrequest;

import com.vorix.gitservice.dto.github.webhook.PullRequestReviewWebhookPayload;

public interface PullRequestReviewService {

    void handleSubmitted(PullRequestReviewWebhookPayload payload);

    void handleEdited(PullRequestReviewWebhookPayload payload);

    void handleDismissed(PullRequestReviewWebhookPayload payload);
}