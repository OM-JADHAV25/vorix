package com.vorix.authservice.service.impl;

import com.vorix.authservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendPlainTextEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);

        log.info("Email sent successfully to={}", to);
    }

    @Override
    public void sendVerificationEmail(String email, String verificationUrl) {

        String body = """
            Welcome to Vorix.

            Please verify your email address using the link below:

            %s

            This link expires in 24 hours.
            """
                .formatted(verificationUrl);

        sendPlainTextEmail(email, "Verify Your Vorix Account", body);
    }
}
