package tech.nx7.vault.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.nx7.vault.dto.CreateSnippetRequest;
import tech.nx7.vault.dto.CreateSnippetResponse;
import tech.nx7.vault.dto.GetSnippetResponse;
import tech.nx7.vault.exception.SnippetNotFoundException;
import tech.nx7.vault.exception.UnauthorizedDeletionException;
import tech.nx7.vault.model.Snippet;
import tech.nx7.vault.repository.SnippetRepository;
import tech.nx7.vault.util.IdGenerator;
import tech.nx7.vault.util.TokenGenerator;

import java.time.Instant;

@Service
public class SnippetServiceImpl implements SnippetService {

    private final SnippetRepository snippetRepository;

    public SnippetServiceImpl(SnippetRepository snippetRepository) {
        this.snippetRepository = snippetRepository;
    }

    @Override
    @Transactional // Ensures the entire method is a single transaction
    public CreateSnippetResponse createSnippet(CreateSnippetRequest request) {
        Snippet snippet = new Snippet();
        snippet.setId(IdGenerator.generateUUIDv7());
        snippet.setRevocationToken(TokenGenerator.generateSecureToken());
        snippet.setCiphertext(request.getCiphertext());
        snippet.setPasswordProtected(request.getPasswordProtected());
        snippet.setExpiresAt(request.getExpiresAt());
        snippet.setMaxViews(request.getMaxViews());
        snippet.setCurrentViews(0);

        Snippet savedSnippet = snippetRepository.save(snippet);

        return new CreateSnippetResponse(savedSnippet.getId(), savedSnippet.getRevocationToken());
    }

    @Override
    @Transactional // This is critical for atomic get-and-delete logic
    public GetSnippetResponse getSnippetAndHandleExpiry(String id) {
        Snippet snippet = snippetRepository.findById(id)
                .orElseThrow(() -> new SnippetNotFoundException("Snippet not found or has already expired."));

        // Check time-based expiry
        if (snippet.getExpiresAt().isBefore(Instant.now())) {
            snippetRepository.delete(snippet);
            throw new SnippetNotFoundException("Snippet has expired.");
        }

        // Increment view count
        snippet.setCurrentViews(snippet.getCurrentViews() + 1);

        // Check view-based expiry
        boolean shouldDelete = snippet.getCurrentViews() >= snippet.getMaxViews();

        int viewsRemaining = snippet.getMaxViews() - snippet.getCurrentViews();

        GetSnippetResponse response = new GetSnippetResponse(
                snippet.getCiphertext(),
                snippet.isPasswordProtected(),
                snippet.getExpiresAt(),
                viewsRemaining
        );

        if (shouldDelete) {
            snippetRepository.delete(snippet);
        } else {
            snippetRepository.save(snippet);
        }

        return response;
    }

    @Override
    @Transactional
    public void revokeSnippet(String id, String revocationToken) {
        Snippet snippet = snippetRepository.findById(id)
                .orElseThrow(() -> new SnippetNotFoundException("Snippet not found."));

        if (!snippet.getRevocationToken().equals(revocationToken)) {
            throw new UnauthorizedDeletionException("Invalid revocation token.");
        }

        snippetRepository.delete(snippet);
    }

    @Override
    @Transactional
    public void purgeExpiredSnippets() {
        // This could be more sophisticated, but a simple delete is fine for now.
        // For very large tables, you might delete in batches.
        snippetRepository.deleteAllByExpiresAtBefore(Instant.now());
    }
}