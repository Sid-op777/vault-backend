package tech.nx7.vault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSnippetResponse {
    private String ciphertext;
    private boolean passwordProtected;
    private Instant expiresAt;
    private int viewsRemaining;
}