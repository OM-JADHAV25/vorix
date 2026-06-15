package com.vorix.gitservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "github_connections",
        indexes = {
                @Index(name = "idx_github_connection_user", columnList = "user_id"),
                @Index(name = "idx_github_connection_installation", columnList = "installation_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubConnection extends BaseEntity {

    @Id
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "installation_id", nullable = false, unique = true)
    private Long installationId;

    @Column(name = "github_user_id", nullable = false, unique = true)
    private Long githubUserId;

    @Column(name = "github_username", nullable = false)
    private String githubUsername;
}
