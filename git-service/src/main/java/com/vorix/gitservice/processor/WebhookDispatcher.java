package com.vorix.gitservice.processor;

public interface WebhookDispatcher {

    void dispatch(String eventType, String webhookPayload);
}