package com.vorix.authservice.service;

public interface EmailService {

    void sendPlainTextEmail(String to, String subject, String body);

    void sendVerificationEmail(String email, String verificationUrl);

    void sendPasswordResetEmail(String email, String resetUrl);
}
