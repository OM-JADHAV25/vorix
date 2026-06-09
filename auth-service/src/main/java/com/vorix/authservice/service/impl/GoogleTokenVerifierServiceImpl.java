package com.vorix.authservice.service.impl;

import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.auth.oauth2.TokenVerifier;
import com.vorix.authservice.dto.oauth.GoogleUserInfo;
import com.vorix.authservice.exception.GoogleAuthenticationException;
import com.vorix.authservice.security.oauth.GoogleOAuthProperties;
import com.vorix.authservice.service.GoogleTokenVerifierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GoogleTokenVerifierServiceImpl implements GoogleTokenVerifierService {

    private final TokenVerifier verifier;

    public GoogleTokenVerifierServiceImpl(GoogleOAuthProperties properties) {

        this.verifier = TokenVerifier.newBuilder()
                                     .setAudience(properties.clientId())
                                     .build();
    }

    @Override
    public GoogleUserInfo verify(String idToken) {

        try {

            JsonWebSignature signature = verifier.verify(idToken);

            JsonWebSignature.Payload payload = signature.getPayload();

            Boolean emailVerified = (Boolean) payload.get("email_verified");

            if (!Boolean.TRUE.equals(emailVerified)) {

                throw new GoogleAuthenticationException("Google email is not verified");
            }

            return new GoogleUserInfo(

                    payload.getSubject(),

                    payload.get("email").toString(),

                    payload.get("name").toString(),

                    true
            );

        } catch (
                TokenVerifier.VerificationException ex
        ) {

            log.warn("Google ID token verification failed", ex);

            throw new GoogleAuthenticationException("Invalid Google ID token", ex);
        }
    }
}