package tech.nx7.vault.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.nx7.vault.model.Snippet;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, String> {

    Optional<Snippet> findByRevocationToken(String revocationToken);

    void deleteAllByExpiresAtBefore(Instant expiryTime);

}