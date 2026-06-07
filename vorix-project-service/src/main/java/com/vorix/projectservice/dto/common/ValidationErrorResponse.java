package com.vorix.projectservice.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValidationErrorResponse(

        boolean success,
        String message,
        String errorCode,
        Map<String, String> errors,
        LocalDateTime timestamp

) {

    public static ValidationErrorResponse of(
            Map<String, String> errors
    ) {

        return ValidationErrorResponse.builder()
                .success(false)
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}