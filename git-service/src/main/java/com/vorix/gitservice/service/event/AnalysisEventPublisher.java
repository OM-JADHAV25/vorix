package com.vorix.gitservice.service.event;

import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;

public interface AnalysisEventPublisher {

    void publish(AnalysisRequestedEvent event);
}