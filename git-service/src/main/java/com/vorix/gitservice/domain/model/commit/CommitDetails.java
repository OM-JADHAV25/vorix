package com.vorix.gitservice.domain.model.commit;

import java.time.Instant;
import java.util.List;

public record CommitDetails(

        String sha,
        String message,
        String authorName,
        String authorEmail,
        Instant authoredAt,
        String committerName,
        String committerEmail,
        Instant committedAt,
        List<ChangedFile> changedFiles
) {
}