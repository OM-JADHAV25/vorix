package com.vorix.gitservice.processor;

public interface IssueCommentProcessor {

    void process(String webhookPayload, String action);
}