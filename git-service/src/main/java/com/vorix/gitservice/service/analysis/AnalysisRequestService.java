package com.vorix.gitservice.service.analysis;

import com.vorix.gitservice.domain.model.analysis.AnalysisContext;

public interface AnalysisRequestService {

    void requestAnalysis(AnalysisContext analysisContext);
}