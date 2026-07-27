package com.vorix.gitservice.dto.request;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;

@Builder
public record GithubWebhookRequest(

        String deliveryId,
        String eventType,
        JsonNode payload
) {
}