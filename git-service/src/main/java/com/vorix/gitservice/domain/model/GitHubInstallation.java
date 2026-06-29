package com.vorix.gitservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "git_hub_installations",
        indexes = {
                @Index(name = "idx_installation_github_id", columnList = "github_installation_id"),
                @Index(name = "idx_installation_account_id", columnList = "github_account_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubInstallation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private Long githubInstallationId;

    @Column(nullable = false)
    private Long githubAccountId;

    @Column(nullable = false)
    private String accountLogin;

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private Boolean active;

    private LocalDateTime suspendedAt;

    @Column(nullable = false)
    private LocalDateTime installedAt;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}