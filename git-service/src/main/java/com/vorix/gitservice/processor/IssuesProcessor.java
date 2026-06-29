package com.vorix.gitservice.processor;

public interface IssuesProcessor {

    void process(String webhookPayload, String action);
}
