package com.vorix.authservice.service;

import com.vorix.authservice.entity.User;

public interface VerificationTokenService {

    String createEmailVerificationToken(User user);
}
