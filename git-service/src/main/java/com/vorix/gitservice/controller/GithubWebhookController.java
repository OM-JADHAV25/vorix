package com.vorix.gitservice.controller;

import com.vorix.gitservice.processor.WebhookDispatcher;
import com.vorix.gitservice.security.GitHubWebhookSignatureValidator;
import com.vorix.gitservice.service.WebhookEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks/github")
@RequiredArgsConstructor
public class GithubWebhookController {

    private final WebhookEventService webhookEventService;
    private final WebhookDispatcher webhookDispatcher;
    private final GitHubWebhookSignatureValidator signatureValidator;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestBody String payload
    ) {

        if (!signatureValidator.isValid(payload, signature)) {
            log.warn("Rejected GitHub webhook due to invalid signature. Delivery ID: {}", deliveryId);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (eventType == null || eventType.isBlank() || deliveryId == null || deliveryId.isBlank()) {
            log.warn("Rejected GitHub webhook due to missing required headers.");
            return ResponseEntity.badRequest().build();
        }

        log.info("Received verified GitHub webhook. Event Type: {}, Delivery ID: {}", eventType, deliveryId);

        webhookEventService.saveWebhookEvent(deliveryId, eventType, payload);

        webhookDispatcher.dispatch(eventType, payload);

        return ResponseEntity.ok().build();
    }
}