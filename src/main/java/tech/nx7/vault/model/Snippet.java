package tech.nx7.vault.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "snippets")
@Getter
@Setter
public class Snippet {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Lob
    @Column(name = "ciphertext", nullable = false, columnDefinition = "TEXT")
    private String ciphertext;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "max_views", nullable = false)
    private int maxViews;

    @Column(name = "current_views", nullable = false)
    private int currentViews = 0;

    @Column(name = "revocation_token", nullable = false, unique = true, length = 64)
    private String revocationToken;

    @Column(name = "password_protected", nullable = false)
    private boolean passwordProtected;
}
