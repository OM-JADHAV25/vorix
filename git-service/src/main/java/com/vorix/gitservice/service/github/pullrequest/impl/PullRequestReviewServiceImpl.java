package com.vorix.gitservice.service.github.pullrequest.impl;

import com.vorix.gitservice.dto.github.webhook.PullRequestReviewWebhookPayload;
import com.vorix.gitservice.service.github.pullrequest.PullRequestReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PullRequestReviewServiceImpl implements PullRequestReviewService {

    @Override
    public void handleSubmitted(PullRequestReviewWebhookPayload payload) {

        log.info(
                "Processed Pull Request Review {} submission for PR #{}",
                payload.review().id(),
                payload.pullRequest().number()
        );
    }

    @Override
    public void handleEdited(PullRequestReviewWebhookPayload payload) {

        log.info("Processed Pull Request Review {} edit.", payload.review().id());
    }

    @Override
    public void handleDismissed(PullRequestReviewWebhookPayload payload) {

        log.info("Processed Pull Request Review {} dismissal.", payload.review().id());
    }
}