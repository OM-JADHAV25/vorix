package com.vorix.authservice.event.listener;

import com.vorix.authservice.event.EmailVerificationEvent;
import com.vorix.authservice.event.PasswordResetEmailEvent;
import com.vorix.authservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePasswordResetEmailEvent(PasswordResetEmailEvent event) {

        try {

            emailService.sendPasswordResetEmail(event.email(), event.resetUrl());

            log.info("Password reset email dispatched to={}", event.email());

        } catch (Exception ex) {

            log.error("Failed to send password reset email to={}", event.email(), ex);
        }

    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailVerificationEvent(EmailVerificationEvent event) {

        try {

            emailService.sendVerificationEmail(event.email(), event.verificationUrl());

            log.info("Verification email dispatched to={}", event.email());

        } catch (Exception ex) {

            log.error("Failed to send verification email to={}", event.email(), ex);
        }
    }
}
