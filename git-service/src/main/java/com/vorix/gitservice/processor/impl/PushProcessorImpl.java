package com.vorix.gitservice.processor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorix.gitservice.domain.model.ConnectedRepository;
import com.vorix.gitservice.domain.model.analysis.AnalysisContext;
import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;
import com.vorix.gitservice.dto.github.webhook.PushWebhookPayload;
import com.vorix.gitservice.service.event.AnalysisEventPublisher;
import com.vorix.gitservice.processor.PushProcessor;
import com.vorix.gitservice.service.ConnectedRepositoryService;
import com.vorix.gitservice.service.analysis.AnalysisContextBuilder;
import com.vorix.gitservice.service.event.AnalysisEventFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushProcessorImpl implements PushProcessor {

    private final ObjectMapper objectMapper;
    private final ConnectedRepositoryService connectedRepositoryService;
    private final AnalysisContextBuilder analysisContextBuilder;
    private final AnalysisEventFactory analysisEventFactory;
    private final AnalysisEventPublisher analysisEventPublisher;

    @Override
    public void process(String webhookPayload) {

        try {

            PushWebhookPayload payload = objectMapper.readValue(webhookPayload, PushWebhookPayload.class);

            ConnectedRepository connectedRepository = connectedRepositoryService.getConnectedRepository(payload.installation().id(), payload.repository().id());

            AnalysisContext analysisContext = analysisContextBuilder.build(
                            connectedRepository.getProjectId(),
                            payload.installation().id(),
                            payload.repository().owner().login(),
                            payload.repository().name(),
                            payload.before(),
                            payload.after()
                    );

            AnalysisRequestedEvent event = analysisEventFactory.create(analysisContext);

            analysisEventPublisher.publish(event);

            log.info("Published analysis request for repository '{}'", payload.repository().fullName());

        } catch (Exception ex) {

            log.error("Failed to process GitHub Push webhook.", ex);
            throw new RuntimeException(ex);
        }
    }
}
