package com.vorix.gitservice.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "connected_repositories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectedRepository extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "project_id", nullable = false)
    private UUID projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "github_connection_id", nullable = false)
    private GithubConnection githubConnection;

    @Column(name = "github_repository_id", nullable = false, unique = true)
    private Long githubRepositoryId;

    @Column(nullable = false)
    private String owner;

    @Column(name = "repository_name", nullable = false)
    private String repositoryName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "is_private", nullable = false)
    private Boolean isPrivate;

    @Column(name = "archived", nullable = false)
    @Builder.Default
    private Boolean archived = false;

    @Column(name = "disabled", nullable =false)
    @Builder.Default
    private Boolean disabled = false;

    @Column(name = "webhook_id")
    private Long webhookId;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "visibility", length = 20)
    private String visibility;

    @Column(name = "html_url", length = 500)
    private String htmlUrl;

    @Column(name = "clone_url", length = 500)
    private String cloneUrl;

    @Column(name = "primary_language")
    private String primaryLanguage;

    @Column(name = "github_created_at")
    private Instant githubCreatedAt;

    @Column(name = "github_updated_at")
    private Instant githubUpdatedAt;


}
