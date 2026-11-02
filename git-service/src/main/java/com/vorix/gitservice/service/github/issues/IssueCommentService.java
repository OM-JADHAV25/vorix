package com.vorix.gitservice.service.github.issues;

import com.vorix.gitservice.dto.github.webhook.IssueCommentWebhookPayload;

public interface IssueCommentService {

    void handleCreated(IssueCommentWebhookPayload payload);

    void handleEdited(IssueCommentWebhookPayload payload);

    void handleDeleted(IssueCommentWebhookPayload payload);
}
