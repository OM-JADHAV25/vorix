package com.vorix.gitservice.service.event.impl;

import com.vorix.gitservice.config.KafkaTopicProperties;
import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;
import com.vorix.gitservice.service.event.AnalysisEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisEventPublisherImpl implements AnalysisEventPublisher {

    private final KafkaTemplate<String, AnalysisRequestedEvent> kafkaTemplate;
    private final KafkaTopicProperties kafkaTopicProperties;

    @Override
    public void publish(AnalysisRequestedEvent event) {

        kafkaTemplate.send(kafkaTopicProperties.getAnalysisRequest(), event.eventId().toString(), event)
                .whenComplete((result, exception) -> {

                    if (exception != null) {

                        log.error("Failed to publish AnalysisRequestedEvent. EventId={}", event.eventId(), exception);
                        return;
                    }

                    log.info(
                            "AnalysisRequestedEvent published successfully. EventId={}, Topic={}, Partition={}, Offset={}",
                            event.eventId(),
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset()
                    );
                });
    }
}