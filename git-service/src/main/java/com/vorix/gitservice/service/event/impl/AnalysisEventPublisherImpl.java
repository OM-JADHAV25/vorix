package com.vorix.gitservice.service.event.impl;

import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;
import com.vorix.gitservice.service.event.AnalysisEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AnalysisEventPublisherImpl implements AnalysisEventPublisher {

    @Override
    public void publish(AnalysisRequestedEvent event) {

        log.info(
                "Publishing AnalysisRequestedEvent | EventId={} | ProjectId={} | Repository={}",
                event.eventId(),
                event.context().projectId(),
                event.context().repository().fullName()
        );

        /*
         * Phase 1:
         * Only log.
         *
         * Phase 2:
         * Publish to Kafka.
         *
         * kafkaTemplate.send(...);
         */
    }
}