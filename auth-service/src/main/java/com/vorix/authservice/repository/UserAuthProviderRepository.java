package com.vorix.authservice.repository;

import com.vorix.authservice.entity.UserAuthProvider;
import com.vorix.authservice.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserAuthProviderRepository extends JpaRepository<UserAuthProvider, UUID> {

    Optional<UserAuthProvider> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
