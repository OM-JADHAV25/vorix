package com.vorix.gitservice.processor;

public interface PullRequestProcessor {

    void process(String webhookPayload, String action);
}
