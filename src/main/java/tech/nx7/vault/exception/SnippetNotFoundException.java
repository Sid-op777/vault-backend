package tech.nx7.vault.exception;

public class SnippetNotFoundException extends RuntimeException {
    public SnippetNotFoundException(String message) {
        super(message);
    }
}