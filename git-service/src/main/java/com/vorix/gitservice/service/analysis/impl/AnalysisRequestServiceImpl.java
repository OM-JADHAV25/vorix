package com.vorix.gitservice.service.analysis.impl;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;
import com.vorix.gitservice.domain.model.event.AnalysisRequestedEvent;
import com.vorix.gitservice.service.analysis.AnalysisRequestService;
import com.vorix.gitservice.service.event.AnalysisEventFactory;
import com.vorix.gitservice.service.event.AnalysisEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisRequestServiceImpl implements AnalysisRequestService {

    private final AnalysisEventFactory analysisEventFactory;
    private final AnalysisEventPublisher analysisEventPublisher;

    @Override
    public void requestAnalysis(AnalysisContext analysisContext) {

        AnalysisRequestedEvent event = analysisEventFactory.create(analysisContext);

        analysisEventPublisher.publish(event);

        log.info(
                "Analysis request published for project {}",
                analysisContext.projectId()
        );
    }
}
