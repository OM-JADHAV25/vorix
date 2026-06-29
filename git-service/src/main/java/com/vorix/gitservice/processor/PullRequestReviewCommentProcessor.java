package com.vorix.gitservice.processor;

public interface PullRequestReviewCommentProcessor {

    void process(String webhookPayload, String action);
}