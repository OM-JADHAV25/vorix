package com.vorix.gitservice.service.analysis.filter;

import com.vorix.gitservice.domain.model.commit.ChangedFile;

public interface AnalysisFileFilter {

    boolean shouldAnalyze(ChangedFile file);
}