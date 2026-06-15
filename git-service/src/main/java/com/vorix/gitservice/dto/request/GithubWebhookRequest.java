package com.vorix.gitservice.dto.request;

import tools.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record GithubWebhookRequest(

        String deliveryId,
        String eventType,
        JsonNode payload
) {
}