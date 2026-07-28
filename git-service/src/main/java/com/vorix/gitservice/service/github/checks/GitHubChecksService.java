package com.vorix.gitservice.service.github.checks;

import com.vorix.gitservice.domain.model.checks.CheckRunConclusion;
import com.vorix.gitservice.domain.model.checks.CheckRunRequest;

public interface GitHubChecksService {

    Long createQueuedCheckRun(CheckRunRequest request);

    void markInProgress(CheckRunRequest request, Long checkRunId);

    void complete(
            CheckRunRequest request,
            Long checkRunId,
            CheckRunConclusion conclusion,
            String title,
            String summary
    );
}