package com.vorix.gitservice.processor;

public interface CheckRunProcessor {

    void process(String webhookPayload, String action);
}