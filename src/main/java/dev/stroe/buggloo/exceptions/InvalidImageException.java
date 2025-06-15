package dev.stroe.buggloo.exceptions;

/**
 * Exception thrown when an invalid image is uploaded.
 */
public class InvalidImageException extends RuntimeException {
    
    public InvalidImageException(String message) {
        super(message);
    }
    
    public InvalidImageException(String message, Throwable cause) {
        super(message, cause);
    }
} 