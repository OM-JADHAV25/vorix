package com.vorix.gitservice.domain.model.commit;

import java.util.List;

public record CommitComparison(
        String baseSha,
        String headSha,
        List<CommitDetails> commits,
        List<ChangedFile> changedFiles
) {
}