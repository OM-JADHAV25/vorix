package com.vorix.projectservice.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(

        boolean success,
        String message,
        String errorCode,
        LocalDateTime timestamp

) {

    public static ErrorResponse of(
            String message,
            String errorCode
    ) {

        return ErrorResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .timestamp(LocalDateTime.now())
                .build();
    }
}