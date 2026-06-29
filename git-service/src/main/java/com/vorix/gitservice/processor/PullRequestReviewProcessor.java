package com.vorix.gitservice.processor;

public interface PullRequestReviewProcessor {

    void process(String webhookPayload, String action);
}