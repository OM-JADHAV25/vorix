package com.vorix.gitservice.service.github.checks.impl;

import com.vorix.gitservice.dto.github.webhook.CheckRunWebhookPayload;
import com.vorix.gitservice.service.github.checks.CheckRunService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CheckRunServiceImpl implements CheckRunService {

    @Override
    public void handleCreated(CheckRunWebhookPayload payload) {

        log.info(
                "Processed Check Run creation. Id={}, Repository={}",
                payload.checkRun().id(),
                payload.repository().fullName()
        );
    }

    @Override
    public void handleRequestedAction(CheckRunWebhookPayload payload) {

        log.info(
                "Processed requested action for Check Run {}",
                payload.checkRun().id()
        );
    }

    @Override
    public void handleRerequested(CheckRunWebhookPayload payload) {

        log.info(
                "Processed rerequested Check Run {}",
                payload.checkRun().id()
        );
    }

    @Override
    public void handleCompleted(CheckRunWebhookPayload payload) {

        log.info(
                "Processed completed Check Run {} with conclusion {}",
                payload.checkRun().id(),
                payload.checkRun().conclusion()
        );
    }
}