package com.vorix.gitservice.service.analysis;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;

import java.util.UUID;

public interface AnalysisContextBuilder {

    AnalysisContext build(
            UUID projectId,
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    );
}