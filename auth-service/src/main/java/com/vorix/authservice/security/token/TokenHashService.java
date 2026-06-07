package com.vorix.authservice.security.token;

public interface TokenHashService {

    String hash(String token);
}
