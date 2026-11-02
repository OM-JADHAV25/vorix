package com.vorix.gitservice.service.github.checks;

import com.vorix.gitservice.dto.github.webhook.CheckRunWebhookPayload;

public interface CheckRunService {

    void handleCreated(CheckRunWebhookPayload payload);

    void handleRequestedAction(CheckRunWebhookPayload payload);

    void handleRerequested(CheckRunWebhookPayload payload);

    void handleCompleted(CheckRunWebhookPayload payload);
}