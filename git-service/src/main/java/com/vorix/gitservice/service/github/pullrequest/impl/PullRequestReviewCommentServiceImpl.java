package com.vorix.gitservice.service.github.pullrequest.impl;

import com.vorix.gitservice.dto.github.webhook.PullRequestReviewCommentWebhookPayload;
import com.vorix.gitservice.service.github.pullrequest.PullRequestReviewCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class PullRequestReviewCommentServiceImpl implements PullRequestReviewCommentService {

    @Override
    public void handleCreated(PullRequestReviewCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub review comment created. Repository={}, PR=#{}, CommentId={}, Path={}, CommitId={}",
                metadata.repository(),
                metadata.pullRequestNumber(),
                metadata.commentId(),
                metadata.path(),
                metadata.commitId()
        );
    }

    @Override
    public void handleEdited(PullRequestReviewCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub review comment edited. Repository={}, PR=#{}, CommentId={}, Path={}",
                metadata.repository(),
                metadata.pullRequestNumber(),
                metadata.commentId(),
                metadata.path()
        );
    }

    @Override
    public void handleDeleted(PullRequestReviewCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub review comment deleted. Repository={}, PR=#{}, CommentId={}",
                metadata.repository(),
                metadata.pullRequestNumber(),
                metadata.commentId()
        );
    }

    private CommentMetadata validateAndExtract(
            PullRequestReviewCommentWebhookPayload payload) {

        if (payload == null) {
            throw new IllegalArgumentException("Pull request review comment payload must not be null");
        }

        if (payload.comment() == null) {
            throw new IllegalArgumentException("Review comment data is missing");
        }

        if (payload.pullRequest() == null) {
            throw new IllegalArgumentException("Pull request data is missing from review comment payload");
        }

        if (payload.repository() == null) {
            throw new IllegalArgumentException("Repository data is missing from review comment payload");
        }

        if (payload.comment().id() == null) {
            throw new IllegalArgumentException("Review comment ID is missing");
        }

        if (payload.pullRequest().number() == null) {
            throw new IllegalArgumentException("Pull request number is missing");
        }

        String repository = payload.repository().fullName();

        if (!StringUtils.hasText(repository)) {
            throw new IllegalArgumentException("Repository full name is missing");
        }

        return new CommentMetadata(
                payload.comment().id(),
                repository,
                payload.pullRequest().number(),
                payload.comment().path(),
                payload.comment().commitId()
        );
    }

    private record CommentMetadata(
            Long commentId,
            String repository,
            Long pullRequestNumber,
            String path,
            String commitId
    ) {}
}
