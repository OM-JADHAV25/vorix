package com.vorix.authservice.service;

import com.vorix.authservice.entity.User;

public interface PasswordResetTokenService {

    String createPasswordResetToken(User user);
}
