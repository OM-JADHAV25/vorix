package com.vorix.authservice.event;

public record PasswordResetEmailEvent(

        String email,
        String resetUrl
) {
}
