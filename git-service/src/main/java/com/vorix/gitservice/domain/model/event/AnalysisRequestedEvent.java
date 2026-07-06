package com.vorix.gitservice.domain.model.event;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;

import java.time.Instant;
import java.util.UUID;

public record AnalysisRequestedEvent(
        UUID eventId,
        Instant createdAt,
        AnalysisContext context
) {
}