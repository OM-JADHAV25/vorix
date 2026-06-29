package com.vorix.gitservice.processor;

public interface RepositorySyncProcessor {

    void process(String webhookPayload);
}
