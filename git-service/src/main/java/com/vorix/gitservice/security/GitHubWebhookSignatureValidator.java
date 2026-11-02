package com.vorix.gitservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Component
public class GitHubWebhookSignatureValidator {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SIGNATURE_PREFIX = "sha256=";

    private final String webhookSecret;

    public GitHubWebhookSignatureValidator(
            @Value("${github.webhook.secret}") String webhookSecret
    ) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            throw new IllegalStateException("GitHub webhook secret is not configured.");
        }

        this.webhookSecret = webhookSecret;
    }

    public boolean isValid(String payload, String signature) {

        if (payload == null || signature == null || !signature.startsWith(SIGNATURE_PREFIX)) {
            return false;
        }

        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);

            SecretKeySpec secretKey = new SecretKeySpec(
                    webhookSecret.getBytes(StandardCharsets.UTF_8),
                    HMAC_SHA256
            );

            mac.init(secretKey);

            byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));

            String expectedSignature = SIGNATURE_PREFIX + HexFormat.of().formatHex(hash);

            return MessageDigest.isEqual(
                    expectedSignature.getBytes(StandardCharsets.UTF_8),
                    signature.getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception ex) {
            throw new IllegalStateException("Failed to verify GitHub webhook signature.", ex);
        }
    }
}