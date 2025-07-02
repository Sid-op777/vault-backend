package tech.nx7.vault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateSnippetResponse {
    private String id;
    private String revocationToken;
}