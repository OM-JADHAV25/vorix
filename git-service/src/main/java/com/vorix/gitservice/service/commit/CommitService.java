package com.vorix.gitservice.service.commit;

import com.vorix.gitservice.domain.model.commit.CommitDetails;
import com.vorix.gitservice.domain.model.commit.CommitComparison;


public interface CommitService {

    CommitDetails getCommit(
            Long installationId,
            String owner,
            String repository,
            String sha
    );

    CommitComparison compareCommits(
            Long installationId,
            String owner,
            String repository,
            String base,
            String head
    );
}