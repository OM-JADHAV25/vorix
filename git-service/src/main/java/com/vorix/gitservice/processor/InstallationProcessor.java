package com.vorix.gitservice.processor;

public interface InstallationProcessor {

    void process(String webhookPayload, String action);
}