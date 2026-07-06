package com.vorix.gitservice.service.analysis.filter.impl;

import com.vorix.gitservice.domain.model.commit.ChangedFile;
import com.vorix.gitservice.service.analysis.filter.AnalysisFileFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AnalysisFileFilterImpl implements AnalysisFileFilter {

    private static final Set<String> IGNORED_EXTENSIONS = Set.of(

            ".png",
            ".jpg",
            ".jpeg",
            ".gif",
            ".svg",
            ".ico",

            ".pdf",

            ".zip",
            ".jar",
            ".war",
            ".ear",

            ".class",
            ".exe",
            ".dll",
            ".so",

            ".mp3",
            ".mp4",
            ".avi",
            ".mov",

            ".lock"
    );

    private static final Set<String> IGNORED_FILENAMES = Set.of(

            "package-lock.json",
            "pnpm-lock.yaml",
            "yarn.lock",
            "bun.lockb",

            ".gitignore",
            ".gitattributes",

            "LICENSE",
            "LICENSE.md",

            "CHANGELOG.md"
    );

    @Override
    public boolean shouldAnalyze(ChangedFile file) {

        String path = file.path().toLowerCase();

        if (IGNORED_FILENAMES.contains(getFileName(path))) {
            return false;
        }

        if (path.contains("/node_modules/")) {
            return false;
        }

        if (path.contains("/vendor/")) {
            return false;
        }

        if (path.contains("/dist/")) {
            return false;
        }

        if (path.contains("/build/")) {
            return false;
        }

        if (path.contains("/target/")) {
            return false;
        }

        if (path.contains("/coverage/")) {
            return false;
        }

        if (path.contains("/generated/")) {
            return false;
        }

        if (path.contains("/.idea/")) {
            return false;
        }

        if (path.contains("/.vscode/")) {
            return false;
        }

        if (path.endsWith(".generated.java")) {
            return false;
        }

        return IGNORED_EXTENSIONS
                .stream()
                .noneMatch(path::endsWith);
    }

    private String getFileName(String path) {

        int index = path.lastIndexOf('/');

        return index == -1
                ? path
                : path.substring(index + 1);
    }
}