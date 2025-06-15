package dev.stroe.buggloo.exceptions;

/**
 * Exception thrown when insect identification fails.
 */
public class InsectIdentificationException extends RuntimeException {
    
    public InsectIdentificationException(String message) {
        super(message);
    }
    
    public InsectIdentificationException(String message, Throwable cause) {
        super(message, cause);
    }
} 