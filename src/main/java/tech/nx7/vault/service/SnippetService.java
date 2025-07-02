package tech.nx7.vault.service;

import tech.nx7.vault.dto.CreateSnippetRequest;
import tech.nx7.vault.dto.CreateSnippetResponse;
import tech.nx7.vault.dto.GetSnippetResponse;

public interface SnippetService {
    CreateSnippetResponse createSnippet(CreateSnippetRequest request);

    GetSnippetResponse getSnippetAndHandleExpiry(String id);

    void revokeSnippet(String id, String revocationToken);

    void purgeExpiredSnippets();
}
