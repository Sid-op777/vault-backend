package tech.nx7.vault.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.nx7.vault.dto.CreateSnippetRequest;
import tech.nx7.vault.dto.CreateSnippetResponse;
import tech.nx7.vault.dto.GetSnippetResponse;
import tech.nx7.vault.service.SnippetService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/snippet")
public class SnippetController {

    private final SnippetService snippetService;

    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    @PostMapping
    public ResponseEntity<CreateSnippetResponse> createSnippet(@Valid @RequestBody CreateSnippetRequest request) {
        CreateSnippetResponse response = snippetService.createSnippet(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetSnippetResponse> getSnippet(@PathVariable String id) {
        GetSnippetResponse response = snippetService.getSnippetAndHandleExpiry(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeSnippet(
            @PathVariable String id,
            @RequestParam("revocation_token") String revocationToken) {
        snippetService.revokeSnippet(id, revocationToken);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}