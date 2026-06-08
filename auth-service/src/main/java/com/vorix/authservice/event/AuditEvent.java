package com.vorix.authservice.event;

import com.vorix.authservice.enums.SecurityEventType;

import java.util.UUID;

public record AuditEvent(

        UUID userId,
        SecurityEventType eventType,
        String details
) {
}
