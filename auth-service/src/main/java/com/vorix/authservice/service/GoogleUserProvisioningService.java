package com.vorix.authservice.service;

import com.vorix.authservice.dto.oauth.GoogleUserInfo;
import com.vorix.authservice.entity.User;

public interface GoogleUserProvisioningService {

    User createGoogleUser(GoogleUserInfo googleUser);
}
