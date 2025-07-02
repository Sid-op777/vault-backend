package tech.nx7.vault.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tech.nx7.vault.exception.SnippetNotFoundException;
import tech.nx7.vault.exception.UnauthorizedDeletionException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SnippetNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleSnippetNotFound(SnippetNotFoundException ex) {
        logger.warn("Snippet not found: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.GONE);
    }

    @ExceptionHandler(UnauthorizedDeletionException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedDeletion(UnauthorizedDeletionException ex) {
        logger.info("Unauthorized deletion attempt: {}", ex.getMessage());
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        logger.debug("Validation failed: {}", errors);
        return new ResponseEntity<>(Map.of("error", "Validation failed", "details", errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        logger.error("An unexpected error occurred", ex);
        return new ResponseEntity<>(Map.of("error", "An internal server error occurred."), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}