package com.vorix.gitservice.service.github.issues.impl;

import com.vorix.gitservice.dto.github.webhook.IssueCommentWebhookPayload;
import com.vorix.gitservice.service.github.issues.IssueCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class IssueCommentServiceImpl implements IssueCommentService {

    @Override
    public void handleCreated(IssueCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub comment created. Repository={}, IssueNumber={}, CommentId={}, Type={}, Author={}",
                metadata.repository(),
                metadata.issueNumber(),
                metadata.commentId(),
                metadata.pullRequest() ? "PULL_REQUEST" : "ISSUE",
                metadata.author()
        );
    }

    @Override
    public void handleEdited(IssueCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub comment edited. Repository={}, IssueNumber={}, CommentId={}, Type={}",
                metadata.repository(),
                metadata.issueNumber(),
                metadata.commentId(),
                metadata.pullRequest() ? "PULL_REQUEST" : "ISSUE"
        );
    }

    @Override
    public void handleDeleted(IssueCommentWebhookPayload payload) {
        CommentMetadata metadata = validateAndExtract(payload);

        log.info(
                "GitHub comment deleted. Repository={}, IssueNumber={}, CommentId={}, Type={}",
                metadata.repository(),
                metadata.issueNumber(),
                metadata.commentId(),
                metadata.pullRequest() ? "PULL_REQUEST" : "ISSUE"
        );
    }

    private CommentMetadata validateAndExtract(
            IssueCommentWebhookPayload payload
    ) {
        if (payload == null) {
            throw new IllegalArgumentException("Issue comment webhook payload must not be null");
        }

        if (payload.comment() == null) {
            throw new IllegalArgumentException("Comment data is missing");
        }

        if (payload.issue() == null) {
            throw new IllegalArgumentException("Issue data is missing");
        }

        if (payload.repository() == null) {
            throw new IllegalArgumentException("Repository data is missing");
        }

        if (payload.comment().id() == null) {
            throw new IllegalArgumentException("Comment ID is missing");
        }

        if (payload.issue().number() == null) {
            throw new IllegalArgumentException("Issue number is missing");
        }

        String repository = payload.repository().fullName();

        if (!StringUtils.hasText(repository)) {
            throw new IllegalArgumentException("Repository full name is missing");
        }

        String author = payload.comment().user() != null ? payload.comment().user().login() : "unknown";

        return new CommentMetadata(
                payload.comment().id(),
                repository,
                payload.issue().number(),
                payload.issue().pullRequest() != null,
                author
        );
    }

    private record CommentMetadata(
            Long commentId,
            String repository,
            Long issueNumber,
            boolean pullRequest,
            String author
    ) {}
}
