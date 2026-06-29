package com.vorix.gitservice.processor.impl;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.JsonNode;
import com.vorix.gitservice.processor.*;
import com.vorix.gitservice.processor.WebhookDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebhookDispatcherImpl implements WebhookDispatcher {

    private final ObjectMapper objectMapper;

    private final InstallationProcessor installationProcessor;
    private final InstallationRepositoriesProcessor installationRepositoriesProcessor;
    private final PushProcessor pushProcessor;
    private final PullRequestProcessor pullRequestProcessor;
    private final IssuesProcessor issuesProcessor;
    private final IssueCommentProcessor issueCommentProcessor;
    private final PullRequestReviewProcessor pullRequestReviewProcessor;
    private final PullRequestReviewCommentProcessor pullRequestReviewCommentProcessor;
    private final CheckRunProcessor checkRunProcessor;
    private final CheckSuiteProcessor checkSuiteProcessor;

    @Override
    public void dispatch(String eventType, String webhookPayload) {

        try {

            JsonNode root = objectMapper.readTree(webhookPayload);

            String action = root.path("action").asText(null);

            log.info("Dispatching GitHub Event. Event={}, Action={}", eventType, action);

            switch (eventType) {

                case "installation" -> installationProcessor.process(webhookPayload, action);

                case "installation_repositories" -> installationRepositoriesProcessor.process(webhookPayload, action);

                case "push" -> pushProcessor.process(webhookPayload);

                case "pull_request" -> pullRequestProcessor.process(webhookPayload, action);

                case "issues" -> issuesProcessor.process(webhookPayload, action);

                case "issue_comment" -> issueCommentProcessor.process(webhookPayload, action);

                case "pull_request_review" -> pullRequestReviewProcessor.process(webhookPayload, action);

                case "pull_request_review_comment" -> pullRequestReviewCommentProcessor.process(webhookPayload, action);

                case "check_run" -> checkRunProcessor.process(webhookPayload, action);

                case "check_suite" -> checkSuiteProcessor.process(webhookPayload, action);

                default -> log.info("Ignoring unsupported GitHub Event '{}'", eventType);
            }

        } catch (Exception ex) {

            log.error("Failed while dispatching GitHub webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}