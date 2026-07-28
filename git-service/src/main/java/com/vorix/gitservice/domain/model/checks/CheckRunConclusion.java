package com.vorix.gitservice.domain.model.checks;

public enum CheckRunConclusion {
    SUCCESS,
    FAILURE,
    NEUTRAL,
    CANCELLED,
    TIMED_OUT,
    ACTION_REQUIRED,
    SKIPPED
}