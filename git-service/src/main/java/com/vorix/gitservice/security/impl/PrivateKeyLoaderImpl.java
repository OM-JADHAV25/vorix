package com.vorix.gitservice.security.impl;

import com.vorix.gitservice.config.GitHubAppProperties;
import com.vorix.gitservice.security.PrivateKeyLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class PrivateKeyLoaderImpl implements PrivateKeyLoader {

    private final ResourceLoader resourceLoader;
    private final GitHubAppProperties properties;

    @Override
    public PrivateKey loadPrivateKey() {

        try {

            Resource resource = resourceLoader.getResource(properties.getPrivateKeyLocation());

            String pem = Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);

            pem = pem
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] decoded = Base64.getDecoder().decode(pem);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

            return KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        } catch (Exception ex) {

            throw new IllegalStateException("Unable to load GitHub App private key.", ex);
        }
    }
}
