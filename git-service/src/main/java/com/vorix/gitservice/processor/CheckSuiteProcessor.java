package com.vorix.gitservice.processor;

public interface CheckSuiteProcessor {

    void process(String webhookPayload, String action);
}