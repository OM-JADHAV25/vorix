package com.vorix.gitservice.processor;

public interface PushProcessor {

    void process(String webhookPayload);
}