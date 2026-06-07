package com.vorix.authservice.security.token.impl;

import com.vorix.authservice.security.token.TokenHashService;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class TokenHashServiceImpl implements TokenHashService {

    @Override
    public String hash(String token) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder builder = new StringBuilder();

            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException ex) {

            throw new IllegalStateException("Unable to hash token", ex);
        }
    }
}
