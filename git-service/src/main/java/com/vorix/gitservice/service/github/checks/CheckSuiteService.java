package com.vorix.gitservice.service.github.checks;

import com.vorix.gitservice.dto.github.webhook.CheckSuiteWebhookPayload;

public interface CheckSuiteService {

    void handleRequested(CheckSuiteWebhookPayload payload);

    void handleRerequested(CheckSuiteWebhookPayload payload);

    void handleCompleted(CheckSuiteWebhookPayload payload);
}