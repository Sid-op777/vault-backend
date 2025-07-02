package tech.nx7.vault.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.Instant;

@Data
public class CreateSnippetRequest {

    @NotBlank(message = "Ciphertext cannot be blank")
    private String ciphertext;

    @NotNull(message = "passwordProtected flag must be provided")
    private Boolean passwordProtected;

    @Future(message = "Expiration date must be in the future")
    @NotNull(message = "expiresAt must be provided")
    private Instant expiresAt;

    @Min(value = 1, message = "maxViews must be at least 1")
    @NotNull(message = "maxViews must be provided")
    private Integer maxViews;
}