package com.vorix.authservice.service.impl;

import com.vorix.authservice.service.GitHubUserProvisioningService;
import com.vorix.authservice.dto.oauth.GitHubUserInfo;
import com.vorix.authservice.entity.Role;
import com.vorix.authservice.entity.User;
import com.vorix.authservice.enums.RoleName;
import com.vorix.authservice.enums.SecurityEventType;
import com.vorix.authservice.exception.ResourceNotFoundException;
import com.vorix.authservice.repository.RoleRepository;
import com.vorix.authservice.repository.UserRepository;
import com.vorix.authservice.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubUserProvisioningServiceImpl implements GitHubUserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditService auditService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public User createGitHubUser(GitHubUserInfo gitHubUser) {

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException("USER role not found"));

        User user = User.builder()
                        .email(gitHubUser.email())
                        .username(generateUsername(gitHubUser.email()))
                        .passwordHash(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .emailVerified(true)
                        .active(true)
                        .accountLocked(false)
                        .deleted(false)
                        .roles(Set.of(userRole))
                        .build();

        User savedUser = userRepository.save(user);

        log.info("Created new GitHub user. UserId={}", savedUser.getId());

        auditService.log(
                savedUser.getId(),
                SecurityEventType.USER_REGISTERED,
                "User registered via GitHub OAuth"
        );

        return savedUser;
    }

    private String generateUsername(String email) {

        String base = email.substring(0, email.indexOf('@'));

        return base + "_"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);
    }

}
