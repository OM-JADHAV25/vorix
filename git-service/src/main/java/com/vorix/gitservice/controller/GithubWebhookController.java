package com.vorix.gitservice.controller;

import com.vorix.gitservice.processor.WebhookDispatcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.vorix.gitservice.service.WebhookEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/webhooks/github")
@RequiredArgsConstructor
public class GithubWebhookController {

    private final WebhookEventService webhookEventService;
    private final WebhookDispatcher webhookDispatcher;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestBody String payload
    ) {

        log.info("Received GitHub webhook. Event Type: {}, Delivery ID: {}", eventType, deliveryId);

        log.debug("Payload: {}", payload);

        webhookEventService.saveWebhookEvent(deliveryId, eventType, payload);

        webhookDispatcher.dispatch(eventType, payload);

        return ResponseEntity.ok().build();
    }
}