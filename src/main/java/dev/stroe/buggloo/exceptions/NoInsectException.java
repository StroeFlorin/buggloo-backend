package dev.stroe.buggloo.exceptions;

/**
 * Exception thrown when no insect is identified.
 */
public class NoInsectException extends RuntimeException {
    
    public NoInsectException(String message) {
        super(message);
    }
    
    public NoInsectException(String message, Throwable cause) {
        super(message, cause);
    }
} 