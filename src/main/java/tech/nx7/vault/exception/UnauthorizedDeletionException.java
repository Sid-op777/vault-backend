package tech.nx7.vault.exception;

public class UnauthorizedDeletionException extends RuntimeException {
    public UnauthorizedDeletionException(String message) {
        super(message);
    }
}