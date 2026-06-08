package com.vorix.authservice.event.listener;

import com.vorix.authservice.entity.SecurityAuditLog;
import com.vorix.authservice.event.AuditEvent;
import com.vorix.authservice.repository.SecurityAuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventListener {

    private final SecurityAuditLogRepository repository;

    @Async
    @org.springframework.context.event.EventListener
    public void handleAuditEvent(AuditEvent event) {

        try {

            SecurityAuditLog logEntity = SecurityAuditLog.builder()
                                         .userId(event.userId())
                                         .eventType(event.eventType())
                                         .eventDetails(event.details())
                                         .createdAt(Instant.now())
                                         .build();

            repository.save(logEntity);

            log.debug("Audit event persisted. EventType={}, UserId={}", event.eventType(), event.userId());

        } catch (Exception ex) {

            log.error("Failed to persist audit event. EventType={}", event.eventType(), ex);
        }
    }
}
