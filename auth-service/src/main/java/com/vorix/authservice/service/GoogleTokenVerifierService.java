package com.vorix.authservice.service;

import com.vorix.authservice.dto.oauth.GoogleUserInfo;

public interface GoogleTokenVerifierService {

    GoogleUserInfo verify(String idToken);
}