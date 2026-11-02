package com.vorix.gitservice.service.github.checks.impl;

import com.vorix.gitservice.dto.github.webhook.CheckSuiteWebhookPayload;
import com.vorix.gitservice.service.github.checks.CheckSuiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CheckSuiteServiceImpl implements CheckSuiteService {

    @Override
    public void handleRequested(CheckSuiteWebhookPayload payload) {

        log.info(
                "Check Suite {} requested for repository {}",
                payload.checkSuite().id(),
                payload.repository().fullName()
        );

        /*
         * Future:
         * - Persist Check Suite state
         * - Correlate with Check Runs
         * - Track lifecycle metrics
         */
    }

    @Override
    public void handleRerequested(CheckSuiteWebhookPayload payload) {

        log.info(
                "Check Suite {} rerequested",
                payload.checkSuite().id()
        );

        /*
         * Future:
         * - Trigger AI re-analysis
         * - Reset Check Run status
         */
    }

    @Override
    public void handleCompleted(CheckSuiteWebhookPayload payload) {

        log.info(
                "Check Suite {} completed with conclusion '{}'",
                payload.checkSuite().id(),
                payload.checkSuite().conclusion()
        );

        /*
         * Future:
         * - Update internal lifecycle
         * - Publish metrics/events
         */
    }
}