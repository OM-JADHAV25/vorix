package com.vorix.gitservice.service;


public interface WebhookEventService {

    void saveWebhookEvent(String deliveryId, String eventType, String payload);
}