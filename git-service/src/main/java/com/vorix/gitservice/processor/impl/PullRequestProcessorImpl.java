package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.enums.PullRequestAction;
import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.domain.model.analysis.AnalysisContext;
import com.vorix.gitservice.dto.github.webhook.PullRequestWebhookPayload;
import com.vorix.gitservice.processor.PullRequestProcessor;
import com.vorix.gitservice.service.ConnectedRepositoryService;
import com.vorix.gitservice.service.analysis.AnalysisContextBuilder;
import com.vorix.gitservice.service.analysis.AnalysisRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PullRequestProcessorImpl implements PullRequestProcessor {

    private final ObjectMapper objectMapper;
    private final ConnectedRepositoryService connectedRepositoryService;
    private final AnalysisContextBuilder analysisContextBuilder;
    private final AnalysisRequestService analysisRequestService;

    @Override
    public void process(
            String webhookPayload,
            String action
    ) {

        try {

            PullRequestWebhookPayload payload = objectMapper.readValue(webhookPayload, PullRequestWebhookPayload.class);

            log.info(
                    "Received Pull Request Event. Action={}, Repository={}, PR=#{}",
                    action,
                    payload.repository().fullName(),
                    payload.number()
            );

            PullRequestAction pullRequestAction = PullRequestAction.valueOf(action.toUpperCase());

            switch (pullRequestAction) {

                case OPENED, SYNCHRONIZE, REOPENED ->log.info("Pull Request requires AI analysis.");

                default -> {

                    log.info("Ignoring Pull Request action '{}'", action);
                    return;
                }
            }

            ConnectedRepository connectedRepository = connectedRepositoryService.getConnectedRepository(payload.installation().id(), payload.repository().id());

            log.info(
                    "Resolved Vorix project {} for repository {}",
                    connectedRepository.getProjectId(),
                    payload.repository().fullName()
            );

            AnalysisContext analysisContext = analysisContextBuilder.build(
                            connectedRepository.getProjectId(),
                            payload.installation().id(),
                            payload.repository().owner().login(),
                            payload.repository().name(),
                            payload.pullRequest().base().sha(),
                            payload.pullRequest().head().sha()
            );

            analysisRequestService.requestAnalysis(analysisContext);

            log.info(
                    "Analysis request published for Pull Request #{} in repository {}",
                    payload.number(),
                    payload.repository().fullName()
            );

        } catch (Exception ex) {

            log.error("Failed to deserialize Pull Request webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}