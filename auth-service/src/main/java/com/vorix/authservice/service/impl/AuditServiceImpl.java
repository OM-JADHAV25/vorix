package com.vorix.authservice.service.impl;

import com.vorix.authservice.enums.SecurityEventType;
import com.vorix.authservice.event.AuditEvent;
import com.vorix.authservice.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void log(UUID userId, SecurityEventType eventType, String details) {

        eventPublisher.publishEvent(new AuditEvent(userId, eventType,details)
        );
    }
}
