package com.vorix.authservice.event;

public record EmailVerificationEvent(

        String email,
        String verificationUrl
) {
}
