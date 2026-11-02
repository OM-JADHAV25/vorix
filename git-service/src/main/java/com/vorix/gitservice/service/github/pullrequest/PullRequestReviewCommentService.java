package com.vorix.gitservice.service.github.pullrequest;

import com.vorix.gitservice.dto.github.webhook.PullRequestReviewCommentWebhookPayload;

public interface PullRequestReviewCommentService {

    void handleCreated(PullRequestReviewCommentWebhookPayload payload);

    void handleEdited(PullRequestReviewCommentWebhookPayload payload);

    void handleDeleted(PullRequestReviewCommentWebhookPayload payload);
}
