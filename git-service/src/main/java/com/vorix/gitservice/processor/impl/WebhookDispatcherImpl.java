package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.vorix.gitservice.processor.*;
import com.vorix.gitservice.processor.WebhookDispatcher;
import com.vorix.gitservice.processor.PullRequestReviewCommentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDispatcherImpl implements WebhookDispatcher {

    private final ObjectMapper objectMapper;

    private final InstallationProcessor installationProcessor;
    private final RepositorySyncProcessor repositorySyncProcessor;
    private final PullRequestProcessor pullRequestProcessor;
    private final PushProcessor pushProcessor;
    private final CheckRunProcessor checkRunProcessor;
    private final CheckSuiteProcessor checkSuiteProcessor;
    private final PullRequestReviewProcessor pullRequestReviewProcessor;
    private final PullRequestReviewCommentProcessor pullRequestReviewCommentProcessor;
    private final IssuesProcessor issuesProcessor;
    private final IssueCommentProcessor issueCommentProcessor;

    @Override
    public void dispatch(String eventType, String webhookPayload) {

        try {

            JsonNode root = objectMapper.readTree(webhookPayload);

            String action = root.path("action").asText(null);

            log.info("Dispatching GitHub Event. Event={}, Action={}", eventType, action);

            switch (eventType) {

                case "installation" -> installationProcessor.process(webhookPayload, action);

                case "installation_repositories" -> repositorySyncProcessor.process(webhookPayload);

                case "push" -> pushProcessor.process(webhookPayload);

                case "pull_request" -> pullRequestProcessor.process(webhookPayload, action);

                case "check_run" -> checkRunProcessor.process(webhookPayload, action);

                case "check_suite" -> checkSuiteProcessor.process(webhookPayload, action);

                case "pull_request_review" -> pullRequestReviewProcessor.process(webhookPayload, action);

                case "pull_request_review_comment" -> pullRequestReviewCommentProcessor.process(webhookPayload, action);

                case "issues" -> issuesProcessor.process(webhookPayload, action);

                case "issue_comment" -> issueCommentProcessor.process(webhookPayload, action);

                default -> log.info("GitHub Event '{}' is not implemented yet.", eventType);
            }

        } catch (Exception ex) {

            log.error("Failed while dispatching GitHub webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}