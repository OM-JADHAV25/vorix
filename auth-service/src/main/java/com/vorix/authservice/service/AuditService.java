package com.vorix.authservice.service;

import com.vorix.authservice.enums.SecurityEventType;

import java.util.UUID;

public interface AuditService {

    void log(UUID userId, SecurityEventType eventType, String details);
}
