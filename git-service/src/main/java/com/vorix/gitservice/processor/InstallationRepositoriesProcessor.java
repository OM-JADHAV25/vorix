package com.vorix.gitservice.processor;

public interface InstallationRepositoriesProcessor {

    void process(String webhookPayload, String action);
}
