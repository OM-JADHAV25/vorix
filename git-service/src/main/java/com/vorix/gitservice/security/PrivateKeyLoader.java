package com.vorix.gitservice.security;

import java.security.PrivateKey;

public interface PrivateKeyLoader {

    PrivateKey loadPrivateKey();
}