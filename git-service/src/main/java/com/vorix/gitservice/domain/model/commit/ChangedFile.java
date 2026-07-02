package com.vorix.gitservice.domain.model.commit;

public record ChangedFile(

        String path,
        String previousPath,
        FileChangeType changeType,
        int additions,
        int deletions,
        int changes,
        String patch
) {
}