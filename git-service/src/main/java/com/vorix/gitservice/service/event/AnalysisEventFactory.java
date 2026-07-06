package com.vorix.gitservice.service.event;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;
import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class AnalysisEventFactory {

    public AnalysisRequestedEvent create(AnalysisContext context) {

        return new AnalysisRequestedEvent(UUID.randomUUID(), Instant.now(), context);
    }
}